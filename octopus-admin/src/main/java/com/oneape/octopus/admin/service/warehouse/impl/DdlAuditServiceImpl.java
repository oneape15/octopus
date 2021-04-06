package com.oneape.octopus.admin.service.warehouse.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.config.SessionThreadLocal;
import com.oneape.octopus.admin.service.warehouse.DatasourceService;
import com.oneape.octopus.admin.service.warehouse.DdlAuditService;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.enums.DdlAuditType;
import com.oneape.octopus.domain.warehouse.DdlAuditInfoDO;
import com.oneape.octopus.mapper.wharehouse.DdlAuditInfoMapper;
import com.oneape.octopus.query.QueryFactory;
import com.oneape.octopus.query.data.DatasourceInfo;
import com.oneape.octopus.query.data.QueryStatus;
import com.oneape.octopus.query.data.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 16:13.
 * Modify:
 */
@Slf4j
@Service
public class DdlAuditServiceImpl implements DdlAuditService {

    @Resource
    private DdlAuditInfoMapper ddlAuditInfoMapper;
    @Resource
    private DatasourceService datasourceService;
    @Resource
    private QueryFactory queryFactory;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int save(DdlAuditInfoDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()),
                I18nMsgConfig.getMessage("ddl.name.empty"));
        if (model.getId() <= 0) {
            model.setId(null);
        }
        int hasSame = ddlAuditInfoMapper.checkSameName(model.getName(), model.getId());
        if (hasSame > 0) {
            throw new BizException(I18nMsgConfig.getMessage("ddl.name.exist"));
        }

        DatasourceInfo dsInfo = Preconditions.checkNotNull(
                datasourceService.getDatasourceInfoById(model.getDatasourceId()),
                I18nMsgConfig.getMessage("ds.id.invalid"));

        // todo Checking warehouse naming rules


        int status;
        if (model.getId() != null) {
            Preconditions.checkNotNull(findById(model.getId()), I18nMsgConfig.getMessage("ddl.id.invalid"));
            status = ddlAuditInfoMapper.update(model);
        } else {
            status = ddlAuditInfoMapper.insert(model);
        }
        return status;
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        DdlAuditInfoDO model = Preconditions.checkNotNull(findById(id), I18nMsgConfig.getMessage("ddl.id.invalid"));
        Preconditions.checkArgument(model.getStatus() == DdlAuditType.ERROR ||
                        model.getStatus() == DdlAuditType.REFUSE ||
                        model.getStatus() == DdlAuditType.AUDIT,
                I18nMsgConfig.getMessage("ddl.del.restrict")
        );

        return ddlAuditInfoMapper.delete(model);
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public DdlAuditInfoDO findById(Long id) {
        return ddlAuditInfoMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<DdlAuditInfoDO> find(DdlAuditInfoDO model) {
        return ddlAuditInfoMapper.list(model);
    }

    /**
     * Execute DDL statements
     *
     * @param ddlId Long
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int exec(Long ddlId) {
        DdlAuditInfoDO model = Preconditions.checkNotNull(
                ddlAuditInfoMapper.findById(ddlId),
                I18nMsgConfig.getMessage("ddl.id.invalid"));

        DatasourceInfo dsInfo = Preconditions.checkNotNull(
                datasourceService.getDatasourceInfoById(model.getDatasourceId()),
                I18nMsgConfig.getMessage("ds.id.invalid"));

        // todo Perform permission checks
        // Checks whether the current user has permission to execute
        Long curUserId = SessionThreadLocal.getUserId();
        model.setExecutor(curUserId);

        // to execute ddl statements
        Result result = queryFactory.execute(dsInfo, model.getDdlText());

        int status;
        if (result.getStatus() == QueryStatus.SUCCESS) {
            status = 1;
            model.setStatus(DdlAuditType.FINISH);
        } else {
            model.setStatus(DdlAuditType.ERROR);
            String errMsg = "An error occurred executing the statement.";
            if (result.getDetailInfo() != null && StringUtils.isNotBlank(result.getDetailInfo().getErrMsg())) {
                errMsg = StringUtils.substring(result.getDetailInfo().getErrMsg(), 0, 500);
            }
            model.setOpinion(errMsg);
            status = 0;
        }

        // Update execution status.
        ddlAuditInfoMapper.update(model);

        return status;
    }

    /**
     * Review DDL statements
     *
     * @param model DdlAuditInfoDO
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int audit(DdlAuditInfoDO model) {
        Preconditions.checkNotNull(
                ddlAuditInfoMapper.findById(model.getId()),
                I18nMsgConfig.getMessage("ddl.id.invalid"));

        // todo Inspection of Audit Authority
        // Checks whether the current user has permission to review.
        Long curUserId = SessionThreadLocal.getUserId();

        // Checks whether the status is valid.
        Preconditions.checkArgument(model.getStatus() != null,
                I18nMsgConfig.getMessage("ddl.status.invalid"));

        Preconditions.checkArgument(
                model.getStatus() == DdlAuditType.REFUSE && StringUtils.isBlank(model.getOpinion()),
                I18nMsgConfig.getMessage("ddl.opinion.empty")
        );

        DdlAuditInfoDO editModel = new DdlAuditInfoDO();
        editModel.setId(model.getId());
        editModel.setOpinion(model.getOpinion());
        editModel.setAuditor(curUserId);

        return ddlAuditInfoMapper.update(editModel);
    }
}

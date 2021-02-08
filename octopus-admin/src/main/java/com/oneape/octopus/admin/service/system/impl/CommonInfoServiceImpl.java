package com.oneape.octopus.admin.service.system.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.constant.OctopusConstant;
import com.oneape.octopus.mapper.system.CommonInfoMapper;
import com.oneape.octopus.domain.system.CommonInfoDO;
import com.oneape.octopus.admin.service.system.CommonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CommonInfoServiceImpl implements CommonInfoService {

    @Resource
    private CommonInfoMapper commonInfoMapper;

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
    public int save(CommonInfoDO model) {
        Preconditions.checkNotNull(model, "The Object is null.");
        Preconditions.checkArgument(StringUtils.isNoneBlank(model.getClassify(), model.getKey(), model.getValue()),
                I18nMsgConfig.getMessage("comm.classifyOrKeyOrValue.empty"));

        if (model.getParentId() == null || model.getParentId() < OctopusConstant.DEFAULT_VALUE) {
            model.setParentId(OctopusConstant.DEFAULT_VALUE);
        } else {
            Preconditions.checkNotNull(commonInfoMapper.findById(model.getParentId()),
                    I18nMsgConfig.getMessage("comm.parent.invalid"));
        }

        Preconditions.checkArgument(
                commonInfoMapper.getSameBy(model.getClassify(), model.getKey(), model.getId()) == 0,
                I18nMsgConfig.getMessage("comm.key.exist"));

        if (model.getId() != null) {
            Preconditions.checkNotNull(
                    commonInfoMapper.findById(model.getId()),
                    I18nMsgConfig.getMessage("comm.id.invalid"));
            return commonInfoMapper.update(model);
        }

        return commonInfoMapper.insert(model);
    }

    /**
     * Get the model information by the primary key.
     *
     * @param id Long
     * @return T
     */
    @Override
    public CommonInfoDO findById(Long id) {
        return commonInfoMapper.findById(id);
    }

    /**
     * Query against an object.
     *
     * @param commonInfo CommonInfoDO
     * @return List
     */
    @Override
    public List<CommonInfoDO> find(CommonInfoDO commonInfo) {
        if (commonInfo == null) {
            commonInfo = new CommonInfoDO();
        }
        return commonInfoMapper.list(commonInfo);
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        Preconditions.checkNotNull(id, I18nMsgConfig.getMessage("comm.id.invalid"));
        CommonInfoDO tmp = new CommonInfoDO();
        tmp.setParentId(id);
        int childrenSize = commonInfoMapper.size(tmp);
        if (childrenSize > 0) {
            throw new BizException(I18nMsgConfig.getMessage("comm.del.restrict"));
        }
        return commonInfoMapper.delete(new CommonInfoDO(id));
    }


    /**
     * Get all classified information.
     *
     * @return List
     */
    @Override
    public List<String> getAllClassify() {
        return commonInfoMapper.getAllClassify();
    }
}

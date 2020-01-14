package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.mapper.report.*;
import com.oneape.octopus.model.DO.report.*;
import com.oneape.octopus.model.VO.ReportColumnVO;
import com.oneape.octopus.model.VO.ReportParamVO;
import com.oneape.octopus.model.VO.ReportVO;
import com.oneape.octopus.service.AccountService;
import com.oneape.octopus.service.ReportService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private GroupRlReportMapper groupRlReportMapper;
    @Resource
    private ReportParamMapper reportParamMapper;
    @Resource
    private ReportColumnMapper reportColumnMapper;
    @Resource
    private ReportSqlMapper reportSqlMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private UIDGeneratorService uidGeneratorService;
    @Resource
    private AccountService accountService;

    /**
     * 根据报表Id获取全量信息
     *
     * @param reportId Long
     * @return ReportVO
     */
    @Override
    public ReportVO findById(Long reportId) {
        ReportDO report = reportMapper.findById(reportId);
        if (report == null) {
            return null;
        }
        ReportVO vo = ReportVO.ofDO(report);
        // 获取查询参数
        List<ReportParamDO> paramDOS = reportParamMapper.list(new ReportParamDO(reportId));
        List<ReportParamVO> params = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(paramDOS)) {
            paramDOS.forEach(pdo -> params.add(ReportParamVO.ofDO(pdo)));
        }
        vo.setParams(params);

        // 获取报表列
        List<ReportColumnDO> columnDOS = reportColumnMapper.list(new ReportColumnDO(reportId));
        List<ReportColumnVO> columns = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(columnDOS)) {
            columnDOS.forEach(cdo -> columns.add(ReportColumnVO.ofDO(cdo)));
        }
        vo.setColumns(columns);

        return vo;
    }

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(ReportDO model) {
        Assert.isTrue(StringUtils.isNotBlank(model.getName()), "报表名称为空");

        if (StringUtils.isBlank(model.getCode())) {
            model.setCode(CodeBuilderUtils.RandmonStr(REPORT_CODE_LEN));
        } else {
            List<ReportDO> list = reportMapper.list(new ReportDO(model.getCode()));
            if (CollectionUtils.isNotEmpty(list)) {
                throw new BizException("报表编码已存在~");
            }
        }
        return reportMapper.insert(model);
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(ReportDO model) {
        Assert.isTrue(StringUtils.isNotBlank(model.getName()), "报表名称为空");
        Assert.isTrue(model.getId() != null, "报表主键为空");

        // 报表code不能被修改
        model.setCode(null);

        return reportMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    @Transactional
    public int deleteById(ReportDO model) {
        Assert.isTrue(model.getId() != null, "报表主键为空");

        int delStatus = reportMapper.delete(model);
        if (delStatus <= 0) {
            return delStatus;
        }
        Long reportId = model.getId();
        // 删除关系
        groupRlReportMapper.delete(new GroupRlReportDO(reportId, null));

        // 删除字段和查询条件
        reportColumnMapper.delete(new ReportColumnDO(reportId));
        reportParamMapper.delete(new ReportParamDO(reportId));

        return delStatus;
    }

    /**
     * 添加报表信息
     *
     * @param reportVO ReportVO
     * @return int 0 - 失败; 1 - 成功;
     */
    @Override
    public int addReportInfo(ReportVO reportVO) {
        ReportDO reportDO = reportVO.toDO();
        int addStatus = insert(reportDO);
        if (addStatus <= 0) {
            throw new BizException("添加报表信息失败");
        }

        if (CollectionUtils.isEmpty(reportVO.getGroupIdList())) {
            return addStatus;
        }

        // 保存报表与报表组的关系
        Long reportId = reportDO.getId();
        for (Long groupId : reportVO.getGroupIdList()) {
            groupRlReportMapper.insert(new GroupRlReportDO(reportId, groupId));
        }

        return addStatus;
    }

    /**
     * 修改报表信息
     *
     * @param reportVO ReportVO
     * @return int 0 - 失败; 1 - 成功;
     */
    @Override
    public int editReportInfo(ReportVO reportVO) {
        ReportDO reportDO = reportVO.toDO();
        int editStatus = edit(reportDO);
        if (editStatus <= 0) {
            throw new BizException("修改报表信息失败");
        }

        Long reportId = reportDO.getId();
        // 判断报表组是否有变更
        List<GroupRlReportDO> groups = groupRlReportMapper.list(new GroupRlReportDO(reportId, null));
        List<Long> oldGroupIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groups)) {
            groups.forEach(g -> oldGroupIds.add(g.getGroupId()));
        }
        List<Long> newGroupIds = reportVO.getGroupIdList();
        if (CollectionUtils.isEmpty(newGroupIds)) {
            // 删除与报表关联的报表组关系
            groupRlReportMapper.delete(new GroupRlReportDO(reportId, null));
        } else {
            // 删除
            Collection<Long> needDelGroupIds = CollectionUtils.removeAll(oldGroupIds, newGroupIds);
            groupRlReportMapper.deleteBy(reportId, new ArrayList<>(needDelGroupIds));

            // 新增
            Collection<Long> needAddGroupIds = CollectionUtils.removeAll(newGroupIds, oldGroupIds);
            needAddGroupIds.forEach(gId -> groupRlReportMapper.insert(new GroupRlReportDO(reportId, gId)));
        }

        return editStatus;
    }

    /**
     * 保存报表查询参数信息
     *
     * @param reportId Long
     * @param params   List
     * @return int 0 - 失败; 1 - 成功;
     */
    @Override
    public int saveReportParams(Long reportId, List<ReportParamDO> params) {
        ReportDO report = reportMapper.findById(reportId);
        if (report == null) {
            throw new BizException("报表信息不存在");
        }

        // 删除旧的查询参数
        reportParamMapper.delete(new ReportParamDO(reportId));

        if (CollectionUtils.isEmpty(params)) {
            return GlobalConstant.SUCCESS;
        }

        // 批量添加新的查询参数
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            ReportParamMapper mapper = session.getMapper(ReportParamMapper.class);
            for (ReportParamDO pdo : params) {
                pdo.setId(uidGeneratorService.getUid());
                pdo.setCreator(accountService.getCurrentUserId());
                mapper.insert(pdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("批量插入报表查询参数异常", e);
            session.rollback();
        } finally {
            session.close();
        }

        return count;
    }

    /**
     * 保存报表列信息
     *
     * @param reportId Long
     * @param columns  List
     * @return int 0 - 失败; 1 - 成功;
     */
    @Override
    public int saveReportColumns(Long reportId, List<ReportColumnDO> columns) {
        ReportDO report = reportMapper.findById(reportId);
        if (report == null) {
            throw new BizException("报表信息不存在");
        }

        // 删除旧的报表列
        reportColumnMapper.delete(new ReportColumnDO(reportId));

        // 新的列为空的话，直接返回成功
        if (CollectionUtils.isEmpty(columns)) {
            return GlobalConstant.SUCCESS;
        }

        // 批量添加新的报表列
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            ReportColumnMapper mapper = session.getMapper(ReportColumnMapper.class);
            for (ReportColumnDO cdo : columns) {
                cdo.setId(uidGeneratorService.getUid());
                cdo.setCreator(accountService.getCurrentUserId());
                mapper.insert(cdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("批量插入报表列信息异常", e);
            session.rollback();
        } finally {
            session.close();
        }

        return count;
    }

    /**
     * 保存报表查询SQL
     *
     * @param reportId  Long
     * @param reportSql ReportSqlDO
     * @return 0 - 失败; 1 - 成功;
     */
    @Override
    public int saveReportSql(Long reportId, ReportSqlDO reportSql) {
        ReportDO report = reportMapper.findById(reportId);
        if (report == null) {
            throw new BizException("报表信息不存在");
        }

        int status;
        if (reportSql.getId() == null || reportSqlMapper.findById(reportSql.getId()) == null) {
            status = reportSqlMapper.insert(reportSql);
            if (status > GlobalConstant.FAIL) {
                report.setReportSqlId(reportSql.getId());
                reportMapper.update(report);
            }
        } else {
            status = reportSqlMapper.update(reportSql);
        }
        return status;
    }
}

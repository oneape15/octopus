package com.oneape.octopus.service.report.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.common.enums.Archive;
import com.oneape.octopus.common.enums.FixOptionType;
import com.oneape.octopus.commons.value.CodeBuilderUtils;
import com.oneape.octopus.commons.value.DataUtils;
import com.oneape.octopus.mapper.report.*;
import com.oneape.octopus.model.DO.report.*;
import com.oneape.octopus.model.VO.*;
import com.oneape.octopus.service.report.ReportService;
import com.oneape.octopus.service.system.AccountService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportMapper        reportMapper;
    @Resource
    private GroupRlReportMapper groupRlReportMapper;
    @Resource
    private ReportParamMapper   reportParamMapper;
    @Resource
    private ReportColumnMapper  reportColumnMapper;
    @Resource
    private ReportSqlMapper     reportSqlMapper;
    @Resource
    private ReportGroupMapper   reportGroupMapper;

    @Resource
    private SqlSessionFactory   sqlSessionFactory;
    @Resource
    private UIDGeneratorService uidGeneratorService;
    @Resource
    private AccountService      accountService;

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
     * 根据对象进行查询
     *
     * @param model ReportDO
     * @return List
     */
    @Override
    public List<ReportVO> find(ReportDO model) {
        List<ReportDO> list = reportMapper.list(model);
        List<ReportVO> vos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(r -> vos.add(ReportVO.ofDO(r)));
        }
        return vos;
    }

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int insert(ReportDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "报表名称为空");

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
     * Modify the data.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    public int edit(ReportDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "报表名称为空");
        Preconditions.checkNotNull(model.getId(), "报表主键为空");

        // 报表code不能被修改
        model.setCode(null);

        return reportMapper.update(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.；
     */
    @Override
    @Transactional
    public int deleteById(ReportDO model) {
        Preconditions.checkNotNull(model.getId(), "报表主键为空");

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
            groupRlReportMapper.deleteByReportId(reportId);
        } else {
            // 删除
            Collection<Long> needDelGroupIds = CollectionUtils.removeAll(oldGroupIds, newGroupIds);
            if (CollectionUtils.isNotEmpty(needDelGroupIds)) {
                groupRlReportMapper.deleteBy(reportId, new ArrayList<>(needDelGroupIds));
            }

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
    public int saveReportParams(Long reportId, List<ReportParamVO> params) {
        Preconditions.checkNotNull(reportMapper.findById(reportId), "报表信息不存在");

        // 删除旧的查询参数
        reportParamMapper.deleteByReportId(reportId);

        if (CollectionUtils.isEmpty(params)) {
            return GlobalConstant.SUCCESS;
        }

        // 批量添加新的查询参数
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            ReportParamMapper mapper = session.getMapper(ReportParamMapper.class);
            for (ReportParamVO tmp : params) {
                ReportParamDO pdo = tmp.toDO();
                pdo.setReportId(reportId);
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
    public int saveReportColumns(Long reportId, List<ReportColumnVO> columns) {
        Preconditions.checkNotNull(reportMapper.findById(reportId), "报表信息不存在");

        // 删除旧的报表列
        reportColumnMapper.deleteByReportId(reportId);

        // 新的列为空的话，直接返回成功
        if (CollectionUtils.isEmpty(columns)) {
            return GlobalConstant.SUCCESS;
        }

        // 批量添加新的报表列
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            ReportColumnMapper mapper = session.getMapper(ReportColumnMapper.class);
            for (ReportColumnVO tmp : columns) {
                ReportColumnDO cdo = tmp.toDO();
                cdo.setReportId(reportId);
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
        ReportDO report = Preconditions.checkNotNull(reportMapper.findById(reportId), "报表信息不存在");
        Preconditions.checkNotNull(reportSql, "报表SQL信息为空");

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

    /**
     * 根据报表Sql主键获取信息
     *
     * @param sqlId Long
     * @return ReportSqlVO
     */
    @Override
    public ReportSqlVO getReportSql(Long sqlId) {
        ReportSqlDO sqlDO = reportSqlMapper.findById(sqlId);
        return ReportSqlVO.ofDO(sqlDO);
    }

    /**
     * 根据指定报表Id复制一张报表
     *
     * @param templateReportId Long
     * @return int
     */
    @Override
    @Transactional
    public int copyReport(Long templateReportId) {
        Preconditions.checkNotNull(templateReportId, "模板报表ID为空");
        ReportDO tmpReport = Preconditions.checkNotNull(reportMapper.findById(templateReportId), "报表信息为空");

        ReportDO newReport = new ReportDO();
        BeanUtils.copyProperties(tmpReport, newReport);

        // 如果SQL信息存在，复制SQL
        Long reportSqlId = null;
        if (tmpReport.getReportSqlId() != null) {
            ReportSqlDO sqlDO = reportSqlMapper.findById(tmpReport.getReportSqlId());
            if (sqlDO != null) {
                ReportSqlDO newSql = new ReportSqlDO();
                BeanUtils.copyProperties(sqlDO, newSql);
                int status = reportSqlMapper.insert(newSql);
                Preconditions.checkArgument(status > 0, "复制报表SQL出现异常");

                reportSqlId = newSql.getId();
            }
        }
        newReport.setReportSqlId(reportSqlId);

        // 插入新的报表信息
        newReport.setOwner(accountService.getCurrentUserId());
        String code = CodeBuilderUtils.RandmonStr(REPORT_CODE_LEN);
        newReport.setCode(code);
        newReport.setName(StringUtils.substringBefore(tmpReport.getName(), "_COPY_") + "_COPY_" + code);
        int status = insert(newReport);

        Preconditions.checkArgument(status > 0, "复制报表信息出现异常");
        Long newReportId = newReport.getId();

        // 插入新报表的查询条件
        List<ReportParamVO> params = getParamByReportId(templateReportId);
        saveReportParams(newReportId, params);
        // 插入报表列信息
        List<ReportColumnVO> columns = getColumnByReportId(templateReportId);
        saveReportColumns(newReportId, columns);

        return status;
    }

    /**
     * 获取报表树型结构(报表组与报表结合)
     *
     * @param lovReport null - 查询所有; 0 - 普通报表; 1 - 简单的报表(LOV);
     * @param filterIds List 过滤掉的报表Id
     * @param fixOption String 携带特定值, NULL, ALL
     * @return List
     */
    @Override
    public List<TreeNodeVO> getReportTree(Integer lovReport, List<Long> filterIds, String fixOption) {
        List<TreeNodeVO> tree = new ArrayList<>();
        FixOptionType fot = FixOptionType.byName(fixOption);
        TreeNodeVO rootNode = fot.getNode();

        // 获取所有报表组信息
        List<ReportGroupDO> groups = reportGroupMapper.list(new ReportGroupDO());
        if (CollectionUtils.isEmpty(groups)) {
            // 报表组不存在，直接返回
            return rootNode == null ? tree : Collections.singletonList(rootNode);
        }

        // 获取所有报表信息
        if (lovReport != null && lovReport > 0) lovReport = 1;
        ReportDO query = new ReportDO();
        query.setLov(lovReport);
        List<ReportVO> reports = reportMapper.findSimpleReport(query);
        if (reports == null) reports = new ArrayList<>();

        Map<Long, List<TreeNodeVO>> groupWithReportsMap = new LinkedHashMap<>();
        reports.forEach(r -> {
            if (filterIds == null || !filterIds.contains(r.getId())) {
                TreeNodeVO node = new TreeNodeVO(r.getId() + "", r.getName(), r.getIcon());
                node.setLeaf(true);
                String groupIds = r.getGroupIds();
                String[] tmps = StringUtils.split(groupIds, ",");
                if (tmps != null) {
                    for (String tmp : tmps) {
                        Long groupId = DataUtils.toLong(tmp, null);
                        if (groupId == null) continue;
                        if (!groupWithReportsMap.containsKey(groupId)) {
                            groupWithReportsMap.put(groupId, new ArrayList<>());
                        }
                        groupWithReportsMap.get(groupId).add(node);
                    }
                }
            }
        });

        Map<Integer, List<ReportGroupDO>> levelMap = new LinkedHashMap<>();
        for (ReportGroupDO group : groups) {
            if (!levelMap.containsKey(group.getLevel())) {
                levelMap.put(group.getLevel(), new ArrayList<>());
            }
            levelMap.get(group.getLevel()).add(group);
        }

        List<Integer> levels = levelMap.keySet()
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // 从下往上遍历
        Map<Long, List<TreeNodeVO>> preLevelMap = new LinkedHashMap<>();
        for (Integer level : levels) {
            Map<Long, List<TreeNodeVO>> curLevelMap = new LinkedHashMap<>();
            for (ReportGroupDO r : levelMap.get(level)) {
                Long id = r.getId();
                List<TreeNodeVO> childrenReport = groupWithReportsMap.get(id);
                List<TreeNodeVO> childrenGroup = preLevelMap.get(id);
                // 空的报表组直接跳过，不返回
                if (CollectionUtils.isEmpty(childrenReport) && CollectionUtils.isEmpty(childrenGroup)) {
                    continue;
                }
                List<TreeNodeVO> childrenAll = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(childrenGroup)) childrenAll.addAll(childrenGroup);
                if (CollectionUtils.isNotEmpty(childrenReport)) childrenAll.addAll(childrenReport);

                TreeNodeVO vo = new TreeNodeVO(id + "", r.getName(), r.getIcon());
                vo.setChildren(childrenAll);
                vo.setLeaf(false);

                Long pId = r.getParentId();
                if (!curLevelMap.containsKey(pId)) {
                    curLevelMap.put(pId, new ArrayList<>());
                }
                curLevelMap.get(pId).add(vo);
            }
            preLevelMap = curLevelMap;
        }

        List<TreeNodeVO> list = new ArrayList<>();
        preLevelMap.values().forEach(list::addAll);

        if (rootNode == null) {
            return list;
        }
        rootNode.setChildren(list);
        return Collections.singletonList(rootNode);
    }

    /**
     * 根据报表Id获取查询参数列表
     *
     * @param reportId Long
     * @return List
     */
    @Override
    public List<ReportParamVO> getParamByReportId(Long reportId) {
        Preconditions.checkNotNull(reportId, "报表Id为空");
        ReportParamDO query = new ReportParamDO(reportId);
        query.setArchive(Archive.NORMAL.value());
        List<ReportParamDO> params = reportParamMapper.list(query);
        List<ReportParamVO> ret = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(params)) {
            params.forEach(rp -> ret.add(ReportParamVO.ofDO(rp)));
        }
        return ret;
    }

    /**
     * 根据报表Id获取字段列表
     *
     * @param reportId Long
     * @return List
     */
    @Override
    public List<ReportColumnVO> getColumnByReportId(Long reportId) {
        Preconditions.checkNotNull(reportId, "报表Id为空");
        ReportColumnDO query = new ReportColumnDO(reportId);
        query.setArchive(Archive.NORMAL.value());
        List<ReportColumnDO> params = reportColumnMapper.list(query);
        List<ReportColumnVO> ret = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(params)) {
            params.forEach(rp -> ret.add(ReportColumnVO.ofDO(rp)));
        }
        return ret;
    }
}

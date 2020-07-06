package com.oneape.octopus.service.report.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.commons.algorithm.Digraph;
import com.oneape.octopus.commons.algorithm.DirectedCycle;
import com.oneape.octopus.commons.value.OptStringUtils;
import com.oneape.octopus.mapper.report.*;
import com.oneape.octopus.model.DO.report.*;
import com.oneape.octopus.model.DTO.ReportDTO;
import com.oneape.octopus.model.VO.report.ReportConfigVO;
import com.oneape.octopus.model.VO.report.args.QueryArg;
import com.oneape.octopus.model.enums.Archive;
import com.oneape.octopus.service.report.ReportService;
import com.oneape.octopus.service.schema.DatasourceService;
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

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    private ReportMapper       reportMapper;
    @Resource
    private ReportParamMapper  reportParamMapper;
    @Resource
    private ReportColumnMapper reportColumnMapper;
    @Resource
    private ReportDslMapper    reportDslMapper;
    @Resource
    private HelpDocumentMapper helpDocumentMapper;

    @Resource
    private SqlSessionFactory   sqlSessionFactory;
    @Resource
    private UIDGeneratorService uidGeneratorService;
    @Resource
    private AccountService      accountService;
    @Resource
    private DatasourceService   datasourceService;

    /**
     * Whether the report Id is valid.
     *
     * @param reportId Long
     * @return boolean true - valid. false - invalid.
     */
    @Override
    public boolean checkReportId(Long reportId) {
        int size = reportMapper.checkReportId(reportId);
        return size > 0;
    }

    /**
     * Get the full amount of information based on the report Id.
     *
     * @param reportId Long
     * @return ReportDTO
     */
    @Override
    public ReportDTO findById(Long reportId) {
        ReportDO report = reportMapper.findById(reportId);
        if (report == null) {
            return null;
        }
        ReportDTO dto = new ReportDTO();
        BeanUtils.copyProperties(report, dto);

        // Gets the report params
        List<ReportParamDO> params = reportParamMapper.findByReportId(reportId);
        dto.setParams(params);

        // Gets the report columns
        List<ReportColumnDO> columns = reportColumnMapper.findByReportId(reportId);
        dto.setColumns(columns);

        // Gets the report dsl information.
        ReportDslDO dsl = reportDslMapper.findByReportId(reportId);
        dto.setDsl(dsl);

        // Gets the report help doc.
        HelpDocumentDO helpDoc = helpDocumentMapper.findByBizInfo(0, reportId);
        dto.setHelpDoc(helpDoc);

        return dto;
    }

    /**
     * Query against an object.
     *
     * @param model ReportDO
     * @return List
     */
    @Override
    public List<ReportDO> find(ReportDO model) {
        Preconditions.checkNotNull(model, "The report object is null.");
        return reportMapper.list(model);
    }

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int save(ReportDO model) {
        Preconditions.checkNotNull(model, "The report object is null.");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "The report name is empty.");
        if (model.getId() != null && model.getId() > 0) {
            boolean valid = checkReportId(model.getId());
            if (!valid) {
                throw new BizException("Invalid report id");
            }
            return reportMapper.update(model);
        }

        return reportMapper.insert(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    @Transactional
    public int deleteById(ReportDO model) {
        Preconditions.checkNotNull(model, "The report object is null.");
        Long reportId = model.getId();
        Preconditions.checkNotNull(reportId, "The report primary key Id is empty.");

        int delStatus = reportMapper.delete(model);

        // delete other information about report.
        if (delStatus > 0) {
            // delete columns.
            reportColumnMapper.deleteByReportId(reportId);
            // delete params.
            reportParamMapper.deleteByReportId(reportId);
            // delete dsl information.
            reportDslMapper.deleteByReportId(reportId);
            // delete help document
            helpDocumentMapper.deleteByBizInfo(0, reportId);
        }

        return delStatus;
    }

    /**
     * Save report information.
     *
     * @param rDto ReportDTO
     * @return int 0 - fail; 1 - success;
     */
    @Override
    @Transactional
    public int saveReportInfo(ReportDTO rDto) {
        int optStatus = save(rDto);

        // save other information about report.
        if (optStatus > 0) {
            // save columns.
            saveReportColumns(rDto.getId(), rDto.getColumns());
            // save params.
            saveReportParams(rDto.getId(), rDto.getParams());
            // save dsl information.
            ReportDslDO dsl = rDto.getDsl();
            if (dsl != null) {
                dsl.setReportId(rDto.getId());
                saveReportDslSql(dsl);
            } else {
                reportDslMapper.deleteByReportId(rDto.getId());
            }

            // save help document
            HelpDocumentDO helpDoc = rDto.getHelpDoc();
            if (helpDoc != null) {
                helpDoc.setBizType(0);
                helpDoc.setBizId(rDto.getId());
                saveHelpDocument(helpDoc);
            } else {
                // delete help document
                helpDocumentMapper.deleteByBizInfo(0, rDto.getId());
            }
        }
        return optStatus;
    }

    /**
     * check the report param whether a required field is empty.
     *
     * @param pdo ReportParamDO
     */
    private void checkParamMustFillField(ReportParamDO pdo) {

    }

    /**
     * Save the report query parameter information.
     *
     * @param reportId Long
     * @param params   List
     * @return int 0 - fail; 1 - success.
     */
    @Transactional
    @Override
    public int saveReportParams(Long reportId, List<ReportParamDO> params) {
        if (CollectionUtils.isEmpty(params)) {
            // Delete the old query parameters.
            reportParamMapper.deleteByReportId(reportId);
            return GlobalConstant.SUCCESS;
        }

        Map<String, List<String>> dependMap = new HashMap<>();
        HashSet<String> allNodes = new HashSet<>();

        // Whether an argument with the same name exists.
        List<String> names = new ArrayList<>();
        for (ReportParamDO p : params) {
            checkParamMustFillField(p);

            if (names.contains(p.getName())) {
                throw new BizException("There are multiple parameters named: " + p.getName());
            } else {
                names.add(p.getName());
            }
            if (StringUtils.isNotBlank(p.getDependOn())) {
                List<String> dependList = OptStringUtils.split(p.getDependOn(), ";");
                if (CollectionUtils.isNotEmpty(dependList)) {
                    allNodes.addAll(dependList);
                    allNodes.add(p.getName());
                    dependMap.put(p.getName(), dependList);
                }
            }
        }

        // Check whether parameter dependencies form loops.
        Iterator<String> iter = checkLinkHasLoop(allNodes, dependMap);
        if (iter != null) {
            String s = "[";
            int index = 0;
            while (iter.hasNext()) {
                if (index++ > 0) {
                    s += ",";
                }
                s += iter.next();
            }
            s += "]";

            throw new BizException("Parameters are interdependent: " + s);
        }


        // Get the has exist params.
        List<ReportParamDO> oldParams = reportParamMapper.list(new ReportParamDO(reportId));

        // Get the need deleted params.
        List<String> needDeleteNames = new ArrayList<>();

        Map<String, ReportParamDO> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(oldParams)) {
            oldParams.forEach(p -> {
                map.put(p.getName(), p);
                if (!names.contains(p.getName())) {
                    needDeleteNames.add(p.getName());
                }
            });
        }

        List<ReportParamDO> addParamList = new ArrayList<>();
        List<ReportParamDO> updateParamList = new ArrayList<>();
        Long sortId = 0L;
        for (ReportParamDO p : params) {
            String name = p.getName();

            p.setReportId(reportId);

            // Reset the sort ID
            p.setSortId(++sortId);

            if (map.containsKey(name)) {
                p.setId(map.get(name).getId());
                updateParamList.add(p);
            } else {
                p.setCreator(accountService.getCurrentUserId());
                p.setId(uidGeneratorService.getUid());
                addParamList.add(p);
            }
        }

        // Remove unwanted parameters
        if (CollectionUtils.isNotEmpty(needDeleteNames)) {
            reportParamMapper.deleteByNames(reportId, needDeleteNames);
        }

        if (CollectionUtils.isEmpty(addParamList) && CollectionUtils.isEmpty(updateParamList)) {
            return 1;
        }

        // bath options
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            ReportParamMapper mapper = session.getMapper(ReportParamMapper.class);
            addParamList.forEach(p -> mapper.insert(p));
            updateParamList.forEach(p -> mapper.update(p));
            session.commit();
        } catch (Exception e) {
            log.error("Bulk insert report query parameter exception.", e);
            session.rollback();
        } finally {
            session.close();
        }

        return 1;
    }

    /**
     * Determine if there are rings in a linked list.
     * eg: 5 --> 3 --> 7 ---> 2 --> 6 -- 8 --> 1 --> 2  , The linked is exist loop.
     *
     * @param linkMap Map
     */
    private Iterator<String> checkLinkHasLoop(HashSet<String> allNode, Map<String, List<String>> linkMap) {
        Digraph<String> digraph = new Digraph<>(allNode.size());
        linkMap.forEach((k, v) -> {
            for (String tmp : v) {
                digraph.addEdge(k, tmp);
            }
        });

        DirectedCycle<String> directedCycle = new DirectedCycle<>(digraph);
        if (directedCycle.hasCycle()) {
            return directedCycle.cycle();
        }
        return null;
    }

    /**
     * Save the report column information.
     *
     * @param reportId Long
     * @param columns  List
     * @return int 0 - fail; 1 - success.
     */
    @Transactional
    @Override
    public int saveReportColumns(Long reportId, List<ReportColumnDO> columns) {
        if (CollectionUtils.isEmpty(columns)) {
            // Delete the old query columns.
            reportColumnMapper.deleteByReportId(reportId);
            return GlobalConstant.SUCCESS;
        }

        // Whether an argument with the same name exists.
        List<String> names = new ArrayList<>();
        for (ReportColumnDO p : columns) {
            if (names.contains(p.getName())) {
                throw new BizException("There are multiple column named: " + p.getName());
            } else {
                names.add(p.getName());
            }
        }

        // Get the has exist columns.
        List<ReportColumnDO> oldColumns = reportColumnMapper.list(new ReportColumnDO(reportId));

        // Get the need deleted columns.
        List<String> needDeleteNames = new ArrayList<>();

        Map<String, ReportColumnDO> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(oldColumns)) {
            oldColumns.forEach(p -> {
                map.put(p.getName(), p);
                if (!names.contains(p.getName())) {
                    needDeleteNames.add(p.getName());
                }
            });
        }

        List<ReportColumnDO> addColumnList = new ArrayList<>();
        List<ReportColumnDO> updateColumnList = new ArrayList<>();
        Long sortId = 0L;
        for (ReportColumnDO p : columns) {
            String name = p.getName();

            p.setReportId(reportId);

            // Reset the sort ID
            p.setSortId(++sortId);

            if (map.containsKey(name)) {
                p.setId(map.get(name).getId());
                updateColumnList.add(p);
            } else {
                p.setCreator(accountService.getCurrentUserId());
                p.setId(uidGeneratorService.getUid());
                addColumnList.add(p);
            }
        }

        // Remove unwanted parameters
        if (CollectionUtils.isNotEmpty(needDeleteNames)) {
            reportColumnMapper.deleteByNames(reportId, needDeleteNames);
        }

        if (CollectionUtils.isEmpty(addColumnList) && CollectionUtils.isEmpty(updateColumnList)) {
            return 1;
        }

        // bath options
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            ReportColumnMapper mapper = session.getMapper(ReportColumnMapper.class);
            addColumnList.forEach(p -> mapper.insert(p));
            updateColumnList.forEach(p -> mapper.update(p));
            session.commit();
        } catch (Exception e) {
            log.error("Bulk insert report query column exception.", e);
            session.rollback();
        } finally {
            session.close();
        }

        return 1;
    }

    /**
     * Save the report query SQL
     *
     * @param reportDsl ReportDslDO
     * @return 0 - fail; 1 - success.
     */
    @Override
    @Transactional
    public int saveReportDslSql(ReportDslDO reportDsl) {
        Preconditions.checkNotNull(reportDsl, "The DSL information is empty.");
        Preconditions.checkNotNull(checkReportId(reportDsl.getReportId()), "The report information is empty.");
        Preconditions.checkArgument(datasourceService.isExistDsId(reportDsl.getDatasourceId()), "The dsId is invalid.");
        int status;
        if (reportDsl.getId() == null || reportDslMapper.findById(reportDsl.getId()) == null) {
            status = reportDslMapper.insert(reportDsl);
        } else {
            status = reportDslMapper.update(reportDsl);
        }
        return status;
    }

    /**
     * Save the help document information.
     *
     * @param hdDo HelpDocumentDO
     * @return 0 - fail; 1 - success;
     */
    @Override
    @Transactional
    public int saveHelpDocument(HelpDocumentDO hdDo) {
        Preconditions.checkNotNull(hdDo, "The Help Document Object is Null.");
        Preconditions.checkArgument(hdDo.getBizId() != null, "Business Id is empty.");
        Preconditions.checkArgument(hdDo.getBizType() != null, "Business types is empty.");
        HelpDocumentDO old = helpDocumentMapper.findByBizInfo(hdDo.getBizType(), hdDo.getBizId());
        if (old != null) {
            hdDo.setId(old.getId());
            return helpDocumentMapper.update(hdDo);
        }
        return helpDocumentMapper.insert(hdDo);
    }

    /**
     * Get DSL information based on the report Id.
     *
     * @param sqlId Long
     * @return ReportDslDO
     */
    @Override
    public ReportDslDO getReportSql(Long sqlId) {
        return reportDslMapper.findById(sqlId);
    }

    /**
     * Get the list of query parameters based on the report Id
     *
     * @param reportId Long
     * @return List
     */
    @Override
    public List<ReportParamDO> getParamByReportId(Long reportId) {
        Preconditions.checkNotNull(reportId, "The report primary key Id is empty.");
        ReportParamDO query = new ReportParamDO(reportId);
        query.setArchive(Archive.NORMAL.value());
        return reportParamMapper.list(query);
    }

    /**
     * Get the list of query columns based on the report Id.
     *
     * @param reportId Long
     * @return List
     */
    @Override
    public List<ReportColumnDO> getColumnByReportId(Long reportId) {
        Preconditions.checkNotNull(reportId, "The report primary key Id is empty.");
        ReportColumnDO query = new ReportColumnDO(reportId);
        query.setArchive(Archive.NORMAL.value());
        return reportColumnMapper.list(query);
    }

    /**
     * Gets report base information for front-end page rendering.
     *
     * @param reportId Long
     * @return ReportConfigVO
     */
    @Override
    public ReportConfigVO getReportConfig(Long reportId) {
        ReportDTO dto = Preconditions.checkNotNull(findById(reportId), "Report does not exist.");

        // base information.
        ReportConfigVO vo = ReportConfigVO.from(dto);

        // the column information.
        vo.setColumns(dto.getColumns());

        // the Front-end query component.
        vo.setArgs(getQueryArg(reportId, dto.getParams()));

        // the report rich text.
        if (dto.getHelpDoc() != null) {
            vo.setHelpDoc(dto.getHelpDoc().getText());
        }

        return vo;
    }

    /**
     * Assemble report parameters into corresponding front-end components.
     *
     * @param reportId Long
     * @param paramDOs List
     * @return List
     */
    private List<QueryArg> getQueryArg(Long reportId, List<ReportParamDO> paramDOs) {
        List<QueryArg> args = new ArrayList<>();
        Map<String, ReportParamDO> mapOfParam = new HashMap<>();
        for (ReportParamDO pdo : paramDOs) {
            QueryArg arg = new QueryArg();
            arg.setName(pdo.getName());
            arg.setLabel(pdo.getAlias());
            arg.setDataType(pdo.getDataType());
            arg.setRequired(pdo.getRequired() == 1);
            arg.setDependOnList(OptStringUtils.split(pdo.getDependOn(), ";"));


            mapOfParam.put(pdo.getName(), pdo);
            args.add(arg);
        }

        return args;
    }
}

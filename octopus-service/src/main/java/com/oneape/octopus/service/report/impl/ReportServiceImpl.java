package com.oneape.octopus.service.report.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.DatetimeMacroUtils;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.commons.algorithm.Digraph;
import com.oneape.octopus.commons.algorithm.DirectedAcyclicGraph;
import com.oneape.octopus.commons.algorithm.DirectedCycle;
import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.commons.dto.Value;
import com.oneape.octopus.commons.value.DateUtils;
import com.oneape.octopus.commons.value.OptStringUtils;
import com.oneape.octopus.commons.value.TitleValueDTO;
import com.oneape.octopus.data.ParseResult;
import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.datasource.ExecParam;
import com.oneape.octopus.datasource.QueryFactory;
import com.oneape.octopus.datasource.data.ColumnHead;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.mapper.report.*;
import com.oneape.octopus.model.DO.report.*;
import com.oneape.octopus.model.DTO.report.ReportDTO;
import com.oneape.octopus.model.DTO.report.ReportParamDTO;
import com.oneape.octopus.model.VO.report.ReportConfigVO;
import com.oneape.octopus.model.VO.report.args.ComponentFactory;
import com.oneape.octopus.model.VO.report.args.QueryArg;
import com.oneape.octopus.model.enums.Archive;
import com.oneape.octopus.model.enums.ReportParamType;
import com.oneape.octopus.parse.ParsingFactory;
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
import java.util.stream.Collectors;

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
    @Resource
    private QueryFactory        queryFactory;
    @Resource
    private ParsingFactory      parsingFactory;

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
        List<ReportParamDTO> dtos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(params)) {
            params.forEach(reportParamDO -> dtos.add(new ReportParamDTO(reportParamDO)));
        }
        dto.setParams(dtos);

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
        if (optStatus <= 0) {
            return optStatus;
        }

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
        return optStatus;
    }

    /**
     * check the report param whether a required field is empty.
     *
     * @param pdo ReportParamDO
     */
    private void checkParamMustFillField(ReportParamDO pdo) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pdo.getName()), "The report param name is empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(pdo.getDataType()), "The report param dataType is empty.");
        Preconditions.checkArgument(pdo.getType() != null, "The report param type is empty.");
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
    public int saveReportParams(Long reportId, List<ReportParamDTO> params) {
        if (CollectionUtils.isEmpty(params)) {
            // Delete the old query parameters.
            reportParamMapper.deleteByReportId(reportId);
            return GlobalConstant.SUCCESS;
        }

        // Whether an argument with the same name exists.
        List<String> names = new ArrayList<>();
        for (ReportParamDO p : params) {
            checkParamMustFillField(p);

            if (names.contains(p.getName())) {
                throw new BizException("There are multiple parameters named: " + p.getName());
            } else {
                names.add(p.getName());
            }
        }

        // Check whether parameter dependencies form loops.
        //
        // Determine if there are rings in a linked list.
        //  5 --> 3 --> 7 ---> 2 --> 6 -- 8 --> 1 --> 2  , The linked is exist loop.
        Digraph<String> digraph = buildDigraph(params);

        DirectedCycle<String> directedCycle = new DirectedCycle<>(digraph);
        if (directedCycle.hasCycle()) {
            String s = OptStringUtils.iterator2String(directedCycle.cycle());
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
            throw new BizException("Bulk insert report query parameter exception.", e);
        } finally {
            session.close();
        }

        return 1;
    }

    /**
     * check the report column whether a required field is empty.
     *
     * @param cdo ReportColumnDO
     */
    private void checkColumnMustFillField(ReportColumnDO cdo) {
        Preconditions.checkArgument(StringUtils.isNotBlank(cdo.getName()), "The report column name is empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(cdo.getDataType()), "The report column dataType is empty.");
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
            checkColumnMustFillField(p);
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

            Long id;
            if (map.containsKey(name)) {
                id = map.get(name).getId();
                updateColumnList.add(p);
            } else {
                id = uidGeneratorService.getUid();
                p.setCreator(accountService.getCurrentUserId());
                addColumnList.add(p);
            }
            p.setId(id);
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
            throw new BizException("Bulk insert report query column exception.", e);
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
     * Gets the contents of the Lov report. The max rows is 1000.
     *
     * @param isStatic    is static lov.
     * @param lovReportId The love report id.
     * @param param       query param
     * @return List
     */
    @Override
    public List<TitleValueDTO> getLovData(boolean isStatic, Long lovReportId, Map<String, Object> param) {
        List<TitleValueDTO> ret = new ArrayList<>();
        ReportDslDO dslDO = Preconditions.checkNotNull(
                reportDslMapper.findByReportId(lovReportId),
                "The report dsl is null.");
        String dslText = dslDO.getText();
        if (StringUtils.isBlank(dslText)) {
            return ret;
        }

        // The lov is static.
        if (isStatic) {
            return JSON.parseArray(dslText, TitleValueDTO.class);
        }

        // Assemble the query parameter types
        Map<String, Value> queryParamMap = new HashMap<>();
        List<ReportParamDO> lovParams = getParamByReportId(lovReportId);
        if (CollectionUtils.isNotEmpty(lovParams)) {
            queryParamMap = lovParams
                    .stream()
                    // Convert the ReportParamDO to ReportParamDTO
                    .map(ReportParamDTO::new)
                    // default, max, min value deal
                    .map(pdo -> {
                        dealReportParamDefaultValue(pdo);
                        return pdo;
                    })
                    // to map
                    .collect(Collectors.toMap(ReportParamDTO::getName, pdo -> {
                        Object defaultVal = pdo.getValDefault();
                        if (param != null && param.containsKey(pdo.getName())) {
                            defaultVal = param.get(pdo.getName());
                        }
                        DataType dt = DataType.byName(pdo.getDataType());
                        if (dt == null) dt = DataType.STRING;

                        Value value = new Value(defaultVal, dt);
                        value.setMulti(ReportParamType.isMultiValue(pdo.getType()));
                        value.setRange(ReportParamType.isRangeValue(pdo.getType()));
                        return value;
                    }));

        }

        // parse the dsl sql
        ParseResult parseResult = parsingFactory.parse(dslText, queryParamMap);

        // to query database.
        DatasourceInfo dsInfo = Preconditions.checkNotNull(
                datasourceService.getDatasourceInfoById(dslDO.getDatasourceId()),
                "The datasource info is not exist.");
        ExecParam execParam = new ExecParam();
        execParam.setNeedTotalSize(false);
        execParam.setLimitSize(1000);
        execParam.setRawSql(parseResult.getRawSql());
        execParam.setParams(parseResult.getValues());

        // Execute SQL, query data.
        Result result = queryFactory.execSql(dsInfo, execParam);

        // Processing query results
        if (result != null && result.isSuccess()) {
            List<Map<String, Object>> rows = result.getRows();
            List<ColumnHead> headList = result.getColumns();
            if (CollectionUtils.isEmpty(rows) || CollectionUtils.isEmpty(headList)) {
                return ret;
            }

            ColumnHead keyHead = headList.get(0);
            ColumnHead titleHead = headList.get(0);
            if (headList.size() > 1) {
                titleHead = headList.get(1);
            }

            for (Map<String, Object> map : rows) {
                Object keyObj = map.get(keyHead.getName());
                Object valueObj = map.get(titleHead.getName());
                ret.add(new TitleValueDTO<>(String.valueOf(keyObj), valueObj));
            }
        }
        return ret;
    }

    /**
     * Build a directed graph of parameter dependencies.
     *
     * @param paramDOs List
     * @return Digraph
     */
    private Digraph<String> buildDigraph(List<ReportParamDTO> paramDOs) {

        // return a empty digraph.
        if (CollectionUtils.isEmpty(paramDOs)) {
            return new Digraph<>(0);
        }

        Map<String, List<String>> dependMap = new HashMap<>();
        HashSet<String> allNodes = new HashSet<>();

        // Build a directed graph.
        for (ReportParamDTO p : paramDOs) {
            if (StringUtils.isBlank(p.getDependOn())) {
                continue;
            }
            List<String> dependList = p.getDependOnList();
            if (CollectionUtils.isNotEmpty(dependList)) {
                allNodes.addAll(dependList);
                allNodes.add(p.getName());
                dependMap.put(p.getName(), dependList);
            }
        }

        Digraph<String> digraph = new Digraph<>(allNodes.size());
        dependMap.forEach((k, v) -> {
            for (String tmp : v) {
                digraph.addEdge(k, tmp);
            }
        });

        return digraph;
    }

    /**
     * Deal the report param valDefault, valMax, valMin attribute.
     *
     * @param pdo The report param object
     */
    private void dealReportParamDefaultValue(ReportParamDO pdo) {
        final String parsePatterns = "yyyy-MM-dd HH:mm:ss";

        // deal the default value
        if (StringUtils.isNotBlank(pdo.getValDefault())) {
            String defaultVal = StringUtils.trim(pdo.getValDefault());
            if (ReportParamType.isMultiValue(pdo.getType())) {
                // Removes single and double quotes from a string.
                defaultVal = StringUtils.replace(defaultVal, "'", "");
                defaultVal = StringUtils.replace(defaultVal, "\"", "");

                // If it is an array string, remove the left and right brackets.
                if (StringUtils.startsWith(defaultVal, "[") && StringUtils.endsWith(defaultVal, "]")) {
                    defaultVal = StringUtils.substring(defaultVal, 1, defaultVal.length() - 1);
                }

                String[] arr = StringUtils.split(defaultVal, ",");

                List<String> defaultList = new ArrayList<>();
                for (String tmp : arr) {
                    Date date = DatetimeMacroUtils.parserMacroValue(tmp, parsePatterns);
                    if (date != null) {
                        defaultList.add(DateUtils.formatDate(date, parsePatterns));
                    }
                }
                pdo.setValDefault("[" + Joiner.on(",").join(defaultList) + "]");
            } else {
                Date date = DatetimeMacroUtils.parserMacroValue(pdo.getValDefault(), parsePatterns);
                if (date != null) {
                    pdo.setValDefault(DateUtils.formatDate(date, parsePatterns));
                }
            }
        }

        // deal the min value
        if (StringUtils.isNotBlank(pdo.getValMin())) {
            Date date = DatetimeMacroUtils.parserMacroValue(pdo.getValMin(), parsePatterns);
            if (date != null) {
                pdo.setValMin(DateUtils.formatDate(date, parsePatterns));
            }
        }

        // deal the max value
        if (StringUtils.isNotBlank(pdo.getValMax())) {
            Date date = DatetimeMacroUtils.parserMacroValue(pdo.getValMax(), parsePatterns);
            if (date != null) {
                pdo.setValMax(DateUtils.formatDate(date, parsePatterns));
            }
        }
    }

    /**
     * Assemble report parameters into corresponding front-end components.
     *
     * @param reportId Long
     * @param paramDOs List
     * @return List
     */
    private List<QueryArg> getQueryArg(Long reportId, List<ReportParamDTO> paramDOs) {
        List<QueryArg> args = new ArrayList<>();
        if (CollectionUtils.isEmpty(paramDOs)) return args;

        // Build a directed graph of parameter dependencies.
        Digraph<String> digraph = buildDigraph(paramDOs);

        // Detects the presence of rings in a directed graph.
        DirectedCycle<String> directedCycle = new DirectedCycle<>(digraph);
        if (directedCycle.hasCycle()) {
            throw new BizException("Parameters are cycle depend.");
        }

        // Get all link line
        DirectedAcyclicGraph<String> dag = new DirectedAcyclicGraph<>(digraph);

        Map<String, ReportParamDTO> map = new LinkedHashMap<>();
        paramDOs.forEach(pdo -> {
            // default, max, min value deal
            dealReportParamDefaultValue(pdo);

            // There are no dependent LOV parameters
            if (ReportParamType.isLov(pdo.getType()) && StringUtils.isBlank(pdo.getDependOn())) {

                // get the values
                List<TitleValueDTO> values = getLovData(ReportParamType.isStaticLov(pdo.getType()), pdo.getLovReportId(), new HashMap<>());
                if (StringUtils.isBlank(pdo.getValDefault()) && CollectionUtils.isNotEmpty(values)) {
                    pdo.setValDefault(String.valueOf(values.get(0).getValue()));
                }
                pdo.setValues(values);
            }

            map.put(pdo.getName(), pdo);
        });

        final int maxLoop = 100;
        int loop = 0;
        Map<String, Boolean> runStateMap = new HashMap<>();

        while (true) {
            // Exceeding the maximum number of cycles.
            if (maxLoop < ++loop) {
                throw new RuntimeException("Exceeding the maximum number of cycles.");
            }

            List<String> todo = new ArrayList<>();
            for (String paramName : dag.getVertex()) {
                if (!runStateMap.containsKey(paramName) || !runStateMap.get(paramName)) {
                    Set<String> prevs = dag.getPrevs(paramName);
                    if (CollectionUtils.isEmpty(prevs)) {
                        todo.add(paramName);
                    } else {
                        boolean toAdd = true;
                        for (String prevVertex : prevs) {
                            if (!runStateMap.containsKey(prevVertex) || !runStateMap.get(prevVertex)) {
                                toAdd = false;
                                break;
                            }
                        }
                        if (toAdd) {
                            todo.add(paramName);
                        }
                    }
                }
            }

            // Break out of the loop if the task to run is empty.
            if (CollectionUtils.isEmpty(todo)) {
                break;
            }

            // run the task.
            for (String paramName : todo) {
                ReportParamDTO pdo = map.get(paramName);

                // have fetch lov data
                if (CollectionUtils.isNotEmpty(pdo.getValues())) {
                    runStateMap.put(paramName, true);
                    continue;
                }

                Map<String, Object> param = new HashMap<>();

                // get the depend param info
                if (StringUtils.isNotBlank(pdo.getDependOn())) {
                    Map<String, String> lovKvMap = pdo.parseLovKvName();
                    List<String> dependList = pdo.getDependOnList();
                    for (String tmp : dependList) {
                        ReportParamDTO dependParam = map.get(tmp);
                        if (dependParam == null) continue;

                        String key = dependParam.getName();
                        if (lovKvMap.containsKey(key)) {
                            key = lovKvMap.get(key);
                        }

                        param.put(key, pdo.getValDefault());
                    }
                }

                List<TitleValueDTO> values = getLovData(ReportParamType.isStaticLov(pdo.getType()), pdo.getLovReportId(), param);
                pdo.setValues(values);
                if (CollectionUtils.isNotEmpty(values)) {
                    pdo.setValDefault(String.valueOf(values.get(0).getValue()));
                }
                runStateMap.put(paramName, true);
            }
        }


        // Assemble the front-end data format.
        for (ReportParamDTO pdo : map.values()) {
            QueryArg arg = new QueryArg();
            arg.setName(pdo.getName());
            arg.setLabel(pdo.getAlias());
            arg.setDataType(pdo.getDataType());
            arg.setRequired(pdo.getRequired() == 1);
            arg.setDependOnList(pdo.getDependOnList());
            arg.setComponent(ComponentFactory.build(pdo));
            args.add(arg);
        }

        return args;
    }
}

package com.oneape.octopus.service.peekdata.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.datasource.*;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.mapper.peekdata.PeekFieldMapper;
import com.oneape.octopus.mapper.peekdata.PeekMapper;
import com.oneape.octopus.mapper.peekdata.PeekRuleMapper;
import com.oneape.octopus.model.DO.peekdata.PeekDO;
import com.oneape.octopus.model.DO.peekdata.PeekFieldDO;
import com.oneape.octopus.model.DO.peekdata.PeekRuleDO;
import com.oneape.octopus.model.DTO.system.UserDTO;
import com.oneape.octopus.model.VO.*;
import com.oneape.octopus.service.peekdata.ModelService;
import com.oneape.octopus.service.peekdata.PeekService;
import com.oneape.octopus.service.schema.DatasourceService;
import com.oneape.octopus.service.system.AccountService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PeekServiceImpl implements PeekService {

    @Resource
    private PeekMapper      peekMapper;
    @Resource
    private PeekFieldMapper peekFieldMapper;
    @Resource
    private PeekRuleMapper  peekRuleMapper;


    @Resource
    private ModelService        modelService;
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

    /**
     * 根据对象进行查询
     *
     * @param model PeekDO
     * @return List
     */
    @Override
    public List<PeekVO> find(PeekDO model) {
        List<PeekVO> vos = new ArrayList<>();
        List<PeekDO> pdos = peekMapper.list(model);
        if (CollectionUtils.isNotEmpty(pdos)) {
            pdos.forEach(p -> vos.add(PeekVO.ofDO(p)));
        }
        return vos;
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
    public int save(PeekDO model) {
        if (model.getId() != null) {
            return peekMapper.update(model);
        }
        return peekMapper.insert(model);
    }

    /**
     * 保存取数实例信息
     *
     * @param model  PeekDO
     * @param fields List 取数返回字段
     * @param rules  List 取数规则
     * @return int 0 - 失败； 1 - 成功；
     */
    @Override
    @Transactional
    public int savePeekInfo(PeekDO model, List<PeekFieldVO> fields, List<PeekRuleVO> rules) {
        Preconditions.checkNotNull(model, "取数实例为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "取数名称为空");
        Long peekId = model.getId();

        // 判断是为修改
        boolean isEdit = true;
        if (peekId == null || peekId <= 0) {
            peekId = null;
            model.setId(null);
            isEdit = false;
        }

        // 判断是否存在相同名称的实例
        List<PeekDO> peeks = peekMapper.list(new PeekDO(peekId, null, model.getName()));
        if (CollectionUtils.isNotEmpty(peeks)) {
            boolean hasSame = true;
            if (isEdit) {
                long size = peeks.stream().filter(p -> !model.getId().equals(p.getId())).count();
                if (size <= 0) {
                    hasSame = false;
                }
            }
            if (hasSame) {
                throw new BizException("存在相同的取数实例名称");
            }
        }

        int status = 0;
        if (isEdit) {
            // 修改
            status = save(model);
            // 删除旧的字段和规则信息
            if (status > 0) {
                peekFieldMapper.delete(new PeekFieldDO(model.getId()));
                peekRuleMapper.delete(new PeekRuleDO(model.getId()));
            }
        } else {
            // 添加
            status = save(model);
        }

        // 批量插入取数字段
        batchInsertPeekField(model.getId(), fields);
        // 批量插入取数规则
        batchInsertPeekRule(model.getId(), rules);

        return status;
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    @Transactional
    public int deleteById(PeekDO model) {
        Preconditions.checkNotNull(model, "取数实例为空");
        int status = peekMapper.delete(model);
        if (status > 0) {
            peekFieldMapper.delete(new PeekFieldDO(model.getId()));
            peekRuleMapper.delete(new PeekRuleDO(model.getId()));
        }
        return status;
    }

    /**
     * 获取数据类型对应的取数规则
     *
     * @return Map
     */
    @Override
    public Map<String, List<Pair<String, String>>> ruleOfDataTypes() {
        Map<String, List<Pair<String, String>>> rules = new LinkedHashMap<>();

        for (DataType dt : DataType.values()) {
            List<Pair<String, String>> list = new ArrayList<>();
            PeekRuleTypeHelper.getRuleByDataType(dt).forEach(r -> list.add(new Pair<>(r.getCode(), r.getDesc())));
            rules.put(dt.name(), list);
        }

        List<Pair<String, String>> list = new ArrayList<>();
        for (PeekRuleTypeHelper rt : PeekRuleTypeHelper.values()) {
            list.add(new Pair<>(rt.getCode(), rt.getDesc()));
        }
        rules.put("ALL_RULES", list);

        return rules;
    }

    /**
     * 根据取数设置，预览结果数据
     *
     * @param modelId Long 模型Id
     * @param fields  List 返回字段信息
     * @param rules   List 取数规则
     * @return Result
     */
    @Override
    public Result previewData(Long modelId, List<PeekFieldVO> fields, List<PeekRuleVO> rules) {
        ModelVO model = Preconditions.checkNotNull(modelService.getById(modelId),
                "取数依赖的模型不存在");

        DatasourceInfo dsi = Preconditions.checkNotNull(
                datasourceService.getDatasourceInfoById(model.getDatasourceId()),
                "数据源信息为空");

        // 组装查询SQL
        String rawSql = assemblePeekDataSql(model, fields, rules);

        // 组装查询条件
        ExecParam param = new ExecParam();
        param.setRawSql(rawSql);
        param.setPageSize(10);
        param.setPageIndex(0);
        param.setNeedTotalSize(true);

        return queryFactory.execSql(dsi, param);
    }

    /**
     * 组装取数SQL
     *
     * @param model  ModeVO
     * @param fields List
     * @param rules  List
     * @return String
     */
    @Override
    public String assemblePeekDataSql(ModelVO model, List<PeekFieldVO> fields, List<PeekRuleVO> rules) {
        if (CollectionUtils.isEmpty(fields)) {
            throw new BizException("取数字列表段为空");
        }
        List<ModelMetaVO> modelMetas = model.getFields();
        if (CollectionUtils.isEmpty(modelMetas)) {
            throw new BizException("模型元素字段为空");
        }
        Map<Long, ModelMetaVO> modelMetaMap = modelMetas
                .stream()
                .collect(Collectors.toMap(ModelMetaVO::getId, Function.identity()));

        /*
         *返回字段分组
         */
        // 测量字段
        List<String> measureFields = new ArrayList<>();
        // 维度字段
        List<String> dimensionFields = new ArrayList<>();
        fields.forEach(field -> {
            ModelMetaVO vo = modelMetaMap.get(field.getMetaId());
            if (vo != null) {
                AggregationOperatorHelper aggHelper = AggregationOperatorHelper.byCode(field.getAggExpression());
                if (aggHelper == null) {
                    dimensionFields.add(vo.getName());
                } else {
                    measureFields.add(aggHelper.toSql(vo.getName()));
                }
            }
        });
        List<String> allFields = new ArrayList<>();
        allFields.addAll(dimensionFields);
        allFields.addAll(measureFields);
        if (CollectionUtils.isEmpty(allFields)) {
            throw new BizException("返回字段为空");
        }

        StringBuilder rawSql = new StringBuilder("SELECT ")
                .append(Joiner.on(",").join(allFields))
                .append(" FROM ")
                .append(model.getTableName());

        // where信息
        List<String> whereSections = new ArrayList<>();
        DataType dataType;
        PeekRuleTypeHelper ruleTypeHelper;
        ModelMetaVO meta;
        for (PeekRuleVO peekRule : rules) {
            meta = modelMetaMap.get(peekRule.getMetaId());
            if (meta == null) continue;

            dataType = DataType.byName(StringUtils.isEmpty(meta.getOriginDataType()) ? meta.getDataType() : meta.getOriginDataType());
            if (dataType == null) continue;

            ruleTypeHelper = PeekRuleTypeHelper.byCode(peekRule.getRule());
            if (ruleTypeHelper == null) continue;

            String tmp = ruleTypeHelper.toSqlSection(dataType, meta.getName(), peekRule.getInputValue());
            if (StringUtils.isNotBlank(tmp)) {
                whereSections.add(tmp);
            }
        }
        if (CollectionUtils.isNotEmpty(whereSections)) {
            rawSql.append(" WHERE ")
                    .append(Joiner.on(" AND ").join(whereSections));
        }

        // group by 信息
        if (CollectionUtils.isNotEmpty(dimensionFields)) {
            rawSql.append(" GROUP BY ")
                    .append(Joiner.on(",").join(dimensionFields));
        }

        return rawSql.toString();
    }

    /**
     * 进行取数
     *
     * @param peekId Long
     * @return 1 - 任务发送成功; 0 - 任务发送失败;
     */
    @Override
    public int peekData(Long peekId) {
        PeekDO peek = Preconditions.checkNotNull(peekMapper.findById(peekId),
                "取数实例信息不存在");
        ModelVO model = Preconditions.checkNotNull(modelService.getById(peek.getModelId()),
                "取数依赖的模型不存在");
        DatasourceInfo dsi = Preconditions.checkNotNull(
                datasourceService.getDatasourceInfoById(model.getDatasourceId()),
                "数据源信息为空");
        List<PeekFieldDO> fieldDOs = Preconditions.checkNotNull(peekFieldMapper.list(new PeekFieldDO(peekId)),
                "取数返回字段列表为空");

        List<PeekFieldVO> fields = new ArrayList<>();
        fieldDOs.forEach(d -> fields.add(PeekFieldVO.ofDO(d)));

        List<PeekRuleDO> ruleDOs = peekRuleMapper.list(new PeekRuleDO(peekId));
        List<PeekRuleVO> rules = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ruleDOs)) {
            ruleDOs.forEach(r -> rules.add(PeekRuleVO.ofDO(r)));
        }

        // 组装查询SQL
        String rawSql = assemblePeekDataSql(model, fields, rules);
        UserDTO user = accountService.getCurrentUser();

        ExportDataParam param = new ExportDataParam();
        param.setEmail(user.getEmail());
        param.setRawSql(rawSql);
        param.setLimitSize(10 * 10000);

        Result result = queryFactory.exportData(dsi, param);
        log.info("导出文件内容为：{}", JSON.toJSONString(result));

        int status = result.getStatus() == Result.QueryStatus.SUCCESS ? 1 : 0;
        if (status > 0) {
            peekMapper.incPeekTime(peekId);
        }
        return status;
    }

    /**
     * 根据Id查询取数详细信息
     *
     * @param peekId Long
     * @return PeekVO
     */
    @Override
    public PeekVO getById(Long peekId) {
        PeekDO peek = Preconditions.checkNotNull(peekMapper.findById(peekId),
                "取数实例信息为空");
        PeekVO vo = PeekVO.ofDO(peek);

        // 返回字段列表
        List<PeekFieldDO> fieldDOs = peekFieldMapper.list(new PeekFieldDO(peekId));
        List<PeekFieldVO> fields = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fieldDOs)) {
            fieldDOs.forEach(d -> fields.add(PeekFieldVO.ofDO(d)));
        }
        vo.setFields(fields);

        // 规则列表
        List<PeekRuleDO> ruleDOs = peekRuleMapper.list(new PeekRuleDO(peekId));
        List<PeekRuleVO> rules = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ruleDOs)) {
            ruleDOs.forEach(r -> rules.add(PeekRuleVO.ofDO(r)));
        }
        vo.setRules(rules);

        return vo;
    }

    /**
     * 批量插入取数规则
     *
     * @param peekId Long
     * @param rules  List
     */
    private void batchInsertPeekRule(Long peekId, List<PeekRuleVO> rules) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            PeekRuleMapper mapper = session.getMapper(PeekRuleMapper.class);
            for (PeekRuleVO pvo : rules) {
                PeekRuleDO pdo = pvo.toDO();
                pdo.setPeekId(peekId);
                pdo.setId(uidGeneratorService.getUid());
                pdo.setCreator(accountService.getCurrentUserId());
                mapper.insert(pdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("批量插入取数规则异常", e);
            session.rollback();
        } finally {
            log.debug("批量插入取数规则：{}条", count);
            session.close();
        }
    }

    /**
     * 批量插入取数字段
     *
     * @param peekId Long
     * @param fields List
     */
    private void batchInsertPeekField(Long peekId, List<PeekFieldVO> fields) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            PeekFieldMapper mapper = session.getMapper(PeekFieldMapper.class);
            for (PeekFieldVO pvo : fields) {
                PeekFieldDO pdo = pvo.toDO();
                pdo.setPeekId(peekId);
                pdo.setId(uidGeneratorService.getUid());
                pdo.setCreator(accountService.getCurrentUserId());
                mapper.insert(pdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("批量插入取数字段异常", e);
            session.rollback();
        } finally {
            log.debug("批量插入取数字段：{}条", count);
            session.close();
        }
    }
}

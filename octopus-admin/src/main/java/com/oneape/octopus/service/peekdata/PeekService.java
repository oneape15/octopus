package com.oneape.octopus.service.peekdata;

import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.model.domain.peekdata.PeekDO;
import com.oneape.octopus.model.VO.ModelVO;
import com.oneape.octopus.model.VO.PeekFieldVO;
import com.oneape.octopus.model.VO.PeekRuleVO;
import com.oneape.octopus.service.BaseService;

import java.util.List;
import java.util.Map;

public interface PeekService extends BaseService<PeekDO> {

    /**
     * 保存取数实例信息
     *
     * @param model  PeekDO
     * @param fields List 取数返回字段
     * @param rules  List 取数规则
     * @return int 0 - 失败； 1 - 成功；
     */
    int savePeekInfo(PeekDO model, List<PeekFieldVO> fields, List<PeekRuleVO> rules);

    /**
     * 获取数据类型对应的取数规则
     *
     * @return Map
     */
    Map<String, List<Pair<String, String>>> ruleOfDataTypes();

    /**
     * 根据取数设置，预览结果数据
     *
     * @param modelId Long 模型Id
     * @param fields  List 返回字段信息
     * @param rules   List 取数规则
     * @return Result
     */
    Result previewData(Long modelId, List<PeekFieldVO> fields, List<PeekRuleVO> rules);

    /**
     * 组装取数SQL
     *
     * @param model  ModeVO
     * @param fields List
     * @param rules  List
     * @return String
     */
    String assemblePeekDataSql(ModelVO model, List<PeekFieldVO> fields, List<PeekRuleVO> rules);

    /**
     * 进行取数
     *
     * @param peekId Long
     * @return 1 - 任务发送成功; 0 - 任务发送失败;
     */
    int peekData(Long peekId);
}

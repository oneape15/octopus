package com.oneape.octopus.service;

import com.oneape.octopus.datasource.PeekRuleTypeHelper;
import com.oneape.octopus.model.DO.peekdata.PeekDO;
import com.oneape.octopus.model.VO.PeekVO;

import java.util.List;
import java.util.Map;

public interface PeekService extends BaseService<PeekDO> {

    /**
     * 根据对象进行查询
     *
     * @param model PeekDO
     * @return List
     */
    List<PeekVO> find(PeekDO model);

    /**
     * 获取数据类型对应的取数规则
     *
     * @return Map
     */
    Map<String, List<PeekRuleTypeHelper>> ruleOfDataTypes();
}

package com.oneape.octopus.service.impl;

import com.google.common.collect.Lists;
import com.oneape.octopus.datasource.DataType;
import com.oneape.octopus.datasource.PeekRuleTypeHelper;
import com.oneape.octopus.mapper.peekdata.PeekMapper;
import com.oneape.octopus.model.DO.peekdata.PeekDO;
import com.oneape.octopus.model.VO.PeekVO;
import com.oneape.octopus.service.PeekService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class PeekServiceImpl implements PeekService {

    private static Map<String, List<PeekRuleTypeHelper>> rules = new LinkedHashMap<>();

    @Resource
    private PeekMapper peekMapper;

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
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(PeekDO model) {
        Assert.isTrue(model != null, "取数实例为空");
        return peekMapper.insert(model);
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(PeekDO model) {
        Assert.isTrue(model != null, "取数实例为空");
        return peekMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(PeekDO model) {
        Assert.isTrue(model != null, "取数实例为空");
        return peekMapper.delete(model);
    }

    /**
     * 获取数据类型对应的取数规则
     *
     * @return Map
     */
    @Override
    public Map<String, List<PeekRuleTypeHelper>> ruleOfDataTypes() {
        Map<String, List<PeekRuleTypeHelper>> rules = new LinkedHashMap<>();

        for (DataType dt : DataType.values()) {
            rules.put(dt.name(), PeekRuleTypeHelper.getRuleByDataType(dt));
        }
        List<PeekRuleTypeHelper> all = new ArrayList<>();
        Collections.addAll(all, PeekRuleTypeHelper.values());
        rules.put("ALL_RULES", all);

        return rules;
    }
}

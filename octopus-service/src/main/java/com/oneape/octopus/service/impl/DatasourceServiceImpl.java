package com.oneape.octopus.service.impl;

import com.oneape.octopus.common.BizException;
import com.oneape.octopus.mapper.report.DatasourceMapper;
import com.oneape.octopus.model.DO.report.DatasourceDO;
import com.oneape.octopus.model.VO.DatasourceVO;
import com.oneape.octopus.service.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Resource
    private DatasourceMapper datasourceMapper;

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int insert(DatasourceDO model) {
        Assert.isTrue(StringUtils.isNotBlank(model.getName()), "数据源名称为空");
        List<DatasourceDO> list = datasourceMapper.list(new DatasourceDO(model.getName()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizException("存在相同名称的数据源");
        }

        return datasourceMapper.insert(model);
    }

    /**
     * 修改数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int edit(DatasourceDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");
        Assert.isTrue(StringUtils.isNotBlank(model.getName()), "数据源名称为空");
        List<DatasourceDO> list = datasourceMapper.list(new DatasourceDO(model.getName()));
        if (CollectionUtils.isNotEmpty(list)) {
            long size = list.stream().filter(ds -> !ds.getId().equals(model.getId())).count();
            if (size > 0) {
                throw new BizException("存在相同名称的数据源");
            }
        }
        return datasourceMapper.update(model);
    }

    /**
     * 根据主键Id删除
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败；
     */
    @Override
    public int deleteById(DatasourceDO model) {
        Assert.isTrue(model.getId() != null, "主键Key为空");
        return datasourceMapper.delete(model);
    }

    /**
     * 根据对象进行查询
     *
     * @param datasource DatasourceDO
     * @return List
     */
    @Override
    public List<DatasourceVO> find(DatasourceDO datasource) {
        Assert.isTrue(datasource != null, "数据源对象为空");
        List<DatasourceDO> ddos = datasourceMapper.list(datasource);

        List<DatasourceVO> dvo = new ArrayList<>();
        if (CollectionUtils.isEmpty(ddos)) {
            return dvo;
        }
        ddos.forEach(ddo -> dvo.add(DatasourceVO.ofDO(ddo)));
        return dvo;
    }
}

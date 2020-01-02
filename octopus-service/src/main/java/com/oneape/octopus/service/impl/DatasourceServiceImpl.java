package com.oneape.octopus.service.impl;

import com.oneape.octopus.mapper.DatasourceMapper;
import com.oneape.octopus.model.DO.DatasourceDO;
import com.oneape.octopus.model.VO.DatasourceVO;
import com.oneape.octopus.service.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Resource
    private DatasourceMapper datasourceMapper;

    /**
     * 添加数据源
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    @Override
    public int addDatasource(DatasourceVO datasource) {
        Assert.isTrue(datasource != null, "数据源对象为空");

        return datasourceMapper.insert(datasource.toDO());
    }

    /**
     * 修改数据源
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    @Override
    public int editDatasource(DatasourceVO datasource) {
        Assert.isTrue(datasource != null, "数据源对象为空");

        return datasourceMapper.update(datasource.toDO());
    }

    /**
     * 删除数据源
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    @Override
    public int delDatasource(DatasourceVO datasource) {
        Assert.isTrue(datasource != null, "数据源对象为空");
        return datasourceMapper.delete(datasource.toDO());
    }

    /**
     * 根据对象进行查询
     *
     * @param datasource DatasourceVO
     * @return int  1- 成功; 0 - 失败
     */
    @Override
    public List<DatasourceVO> find(DatasourceVO datasource) {
        Assert.isTrue(datasource != null, "数据源对象为空");
        List<DatasourceDO> ddos = datasourceMapper.list(datasource.toDO());

        List<DatasourceVO> dvo = new ArrayList<>();
        if (CollectionUtils.isEmpty(ddos)) {
            return dvo;
        }
        ddos.forEach(ddo -> {
            DatasourceVO vo = DatasourceVO.ofDO(ddo);
            dvo.add(vo);
        });
        return dvo;
    }
}

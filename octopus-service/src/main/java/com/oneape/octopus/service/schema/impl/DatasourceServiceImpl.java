package com.oneape.octopus.service.schema.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.common.enums.Archive;
import com.oneape.octopus.commons.security.PBEUtils;
import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.mapper.schema.DatasourceMapper;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import com.oneape.octopus.model.VO.DatasourceVO;
import com.oneape.octopus.service.schema.DatasourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.oneape.octopus.commons.constant.OctopusConstant.PWD_MASK_TAG;

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
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "数据源名称为空");
        List<DatasourceDO> list = datasourceMapper.list(new DatasourceDO(model.getName()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizException("存在相同名称的数据源");
        }

        // 对密码进行加密
        if (StringUtils.isNotBlank(model.getPassword()) && StringUtils.startsWith(model.getPassword(), PWD_MASK_TAG)) {
            model.setPassword(PWD_MASK_TAG + PBEUtils.encrypt(model.getPassword()));
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
        Preconditions.checkNotNull(model.getId(), "主键Key为空");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "数据源名称为空");
        List<DatasourceDO> list = datasourceMapper.list(new DatasourceDO(model.getName()));
        if (CollectionUtils.isNotEmpty(list)) {
            long size = list.stream().filter(ds -> !ds.getId().equals(model.getId())).count();
            if (size > 0) {
                throw new BizException("存在相同名称的数据源");
            }
        }

        // 对密码进行加密
        if (StringUtils.isNotBlank(model.getPassword()) && StringUtils.startsWith(model.getPassword(), PWD_MASK_TAG)) {
            model.setPassword(PWD_MASK_TAG + PBEUtils.encrypt(model.getPassword()));
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
        Preconditions.checkNotNull(model.getId(), "主键Key为空");
        return datasourceMapper.delete(model);
    }

    /**
     * 根据对象进行查询
     *
     * @param datasource DatasourceDO
     * @return List
     */
    @Override
    public List<DatasourceDO> find(DatasourceDO datasource) {
        Preconditions.checkNotNull(datasource, "数据源对象为空");
        datasource.setArchive(Archive.NORMAL.value());
        List<DatasourceDO> ddos = datasourceMapper.list(datasource);
        if (ddos == null) {
            return new ArrayList<>();
        }
        return ddos;
    }

    /**
     * 根据Id获取数据源信息
     *
     * @param dsId Long
     * @return DatasourceDO
     */
    @Override
    public DatasourceDO findById(Long dsId) {
        return datasourceMapper.findById(dsId);
    }

    /**
     * 根据Id获取数据源信息
     *
     * @param dsId Long
     * @return DatasourceInfo
     */
    @Override
    public DatasourceInfo getDatasourceInfoById(Long dsId) {
        DatasourceDO ddo = findById(dsId);
        if (ddo == null) {
            return null;
        }
        DatasourceInfo dsi = new DatasourceInfo();
        dsi.setDatasourceType(DatasourceTypeHelper.byName(ddo.getType()));
        dsi.setUsername(ddo.getUsername());
        dsi.setPassword(ddo.getPassword());
        dsi.setUrl(ddo.getJdbcUrl());

        return dsi;
    }
}

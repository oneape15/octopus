package com.oneape.octopus.service.schema.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.model.enums.Archive;
import com.oneape.octopus.commons.security.PBEUtils;
import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.mapper.schema.DatasourceMapper;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
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
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int insert(DatasourceDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "The data source name is empty.");
        List<DatasourceDO> list = datasourceMapper.list(new DatasourceDO(model.getName()));
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BizException("The same data source name exists.");
        }

        // Encrypt the password
        if (StringUtils.isNotBlank(model.getPassword()) && StringUtils.startsWith(model.getPassword(), PWD_MASK_TAG)) {
            model.setPassword(PWD_MASK_TAG + PBEUtils.encrypt(model.getPassword()));
        }

        return datasourceMapper.insert(model);
    }

    /**
     * edited the data source information.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int edit(DatasourceDO model) {
        Preconditions.checkNotNull(model.getId(), "The primary Key is empty.");
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "The data source name is empty.");
        List<DatasourceDO> list = datasourceMapper.list(new DatasourceDO(model.getName()));
        if (CollectionUtils.isNotEmpty(list)) {
            long size = list.stream().filter(ds -> !ds.getId().equals(model.getId())).count();
            if (size > 0) {
                throw new BizException("The same data source name exists.");
            }
        }

        // Encrypt the password
        if (StringUtils.isNotBlank(model.getPassword()) && StringUtils.startsWith(model.getPassword(), PWD_MASK_TAG)) {
            model.setPassword(PWD_MASK_TAG + PBEUtils.encrypt(model.getPassword()));
        }
        return datasourceMapper.update(model);
    }

    /**
     * Delete by primary key Id.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(DatasourceDO model) {
        Preconditions.checkNotNull(model.getId(), "The primary Key is empty.");
        return datasourceMapper.delete(model);
    }

    /**
     * Query by object.
     *
     * @param datasource DatasourceDO
     * @return List
     */
    @Override
    public List<DatasourceDO> find(DatasourceDO datasource) {
        Preconditions.checkNotNull(datasource, "The data source Object is empty.");
        datasource.setArchive(Archive.NORMAL.value());
        List<DatasourceDO> ddos = datasourceMapper.list(datasource);
        if (ddos == null) {
            return new ArrayList<>();
        }
        return ddos;
    }

    /**
     * Query by data source information  based on the primary key.
     *
     * @param dsId Long
     * @return DatasourceDO
     */
    @Override
    public DatasourceDO findById(Long dsId) {
        return datasourceMapper.findById(dsId);
    }

    /**
     * Get the data source information based on the Id.
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
        dsi.setId(dsId);
        dsi.setDatasourceType(DatasourceTypeHelper.byName(ddo.getType()));
        dsi.setUsername(ddo.getUsername());
        dsi.setPassword(ddo.getPassword());
        dsi.setUrl(ddo.getJdbcUrl());
        dsi.setTestSql(ddo.getTestSql());

        return dsi;
    }
}

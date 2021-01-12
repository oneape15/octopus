package com.oneape.octopus.service.schema.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.commons.security.PBEUtils;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.datasource.data.DatasourceInfo;
import com.oneape.octopus.domain.schema.DatasourceDO;
import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.job.SchemaSyncJob;
import com.oneape.octopus.mapper.schema.DatasourceMapper;
import com.oneape.octopus.service.schema.DatasourceService;
import com.oneape.octopus.service.task.QuartzTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oneape.octopus.commons.constant.OctopusConstant.PWD_MASK_TAG;

@Slf4j
@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Resource
    private DatasourceMapper  datasourceMapper;
    @Resource
    private QuartzTaskService quartzTaskService;

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int save(DatasourceDO model) {
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getName()), "The data source name is empty.");
        if (model.getId() <= 0) {
            model.setId(null);
        }
        int hasSame = datasourceMapper.getSameBy(model.getName(), model.getId());
        if (hasSame > 0) {
            throw new BizException("The same data source name exists.");
        }

        // Encrypt the password
        if (StringUtils.isNotBlank(model.getPassword()) && !StringUtils.startsWith(model.getPassword(), PWD_MASK_TAG)) {
            model.setPassword(PWD_MASK_TAG + PBEUtils.encrypt(model.getPassword()));
        }

        int status;
        String oldCron = null;
        DatasourceDO oldDdo = null;
        if (model.getId() != null) {
            oldDdo = Preconditions.checkNotNull(datasourceMapper.findById(model.getId()), "The data source id is invalid.");
            oldCron = oldDdo.getCron();
            status = datasourceMapper.update(model);
        } else {
            status = datasourceMapper.insert(model);
        }

        if (status <= 0) {
            return status;
        }

        // update the cron job
        if (StringUtils.isNotBlank(oldCron)) {
            if (StringUtils.isBlank(model.getCron())) {
                quartzTaskService.deleteJob2Schedule(schema2QuartzTaskDO(oldDdo));
            } else {
                quartzTaskService.updateJob2Schedule(schema2QuartzTaskDO(model), buildJobDataMap(model));
            }
        } else {
            if (StringUtils.isNotBlank(model.getCron())) {
                quartzTaskService.addJob2Schedule(schema2QuartzTaskDO(model), buildJobDataMap(model));
            }
        }

        return status;
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        Preconditions.checkNotNull(id, "The primary Key is empty.");
        return datasourceMapper.delete(new DatasourceDO(id));
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

    /**
     * Checks if the dsId is valid.
     *
     * @param dsId Long
     * @return boolean true - valid ; false - invalid.
     */
    @Override
    public boolean isExistDsId(Long dsId) {
        return datasourceMapper.isExistDsId(dsId) > 0;
    }

    /**
     * change the datasource status.
     *
     * @param dsId   Long
     * @param status Integer
     * @return boolean true - success, false - fail.
     */
    @Override
    public boolean changeStatus(Long dsId, Integer status) {
        Preconditions.checkNotNull(datasourceMapper.findById(dsId), "The data source id is invalid.");
        DatasourceDO ddo = new DatasourceDO();
        ddo.setId(dsId);
        ddo.setStatus(status);
        return datasourceMapper.update(ddo) > 0;
    }

    /**
     * Gets data source information by name.
     *
     * @param name String
     * @return DatasourceDO
     */
    @Override
    public List<DatasourceDO> findByName(String name) {
        DatasourceDO model = new DatasourceDO();
        model.setName(name);
        return datasourceMapper.list(model);
    }

    /**
     * Initializes the synchronous Job
     */
    @Override
    public void initSyncJob() {
        List<DatasourceDO> ddoList = datasourceMapper.getNeedSyncDatasourceList();
        if (CollectionUtils.isEmpty(ddoList)) {
            return;
        }

        for (DatasourceDO ddo : ddoList) {
            QuartzTaskDO qt = schema2QuartzTaskDO(ddo);


            quartzTaskService.addJob2Schedule(qt, buildJobDataMap(ddo));
        }
    }

    /**
     * build the job data map.
     *
     * @param ddo DatasourceDO
     * @return Map
     */
    private Map<String, Object> buildJobDataMap(DatasourceDO ddo) {
        Map<String, Object> jobDataMap = new HashMap<>();
        jobDataMap.put("dsId", ddo.getId());
        return jobDataMap;
    }


    /**
     * build the task object.
     *
     * @param ddo DatasourceDO
     * @return QuartzTaskDO
     */
    private QuartzTaskDO schema2QuartzTaskDO(DatasourceDO ddo) {
        QuartzTaskDO qt = new QuartzTaskDO();
        qt.setTaskName(ddo.getId() + "");
        qt.setCron(ddo.getCron());
        qt.setGroupName("SCHEMA_GROUP");
        qt.setStatus(1);
        qt.setJobClass(SchemaSyncJob.class.getName());

        return qt;
    }
}

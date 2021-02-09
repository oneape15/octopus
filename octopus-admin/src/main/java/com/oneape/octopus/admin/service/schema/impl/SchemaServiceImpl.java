package com.oneape.octopus.admin.service.schema.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.job.SchemaTableSyncJob;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.query.QueryFactory;
import com.oneape.octopus.query.data.DatasourceInfo;
import com.oneape.octopus.query.schema.SchemaTable;
import com.oneape.octopus.query.schema.SchemaTableField;
import com.oneape.octopus.domain.schema.TableColumnDO;
import com.oneape.octopus.domain.schema.TableSchemaDO;
import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.mapper.schema.TableColumnMapper;
import com.oneape.octopus.mapper.schema.TableSchemaMapper;
import com.oneape.octopus.admin.service.schema.DatasourceService;
import com.oneape.octopus.admin.service.schema.SchemaService;
import com.oneape.octopus.admin.service.task.QuartzTaskService;
import com.oneape.octopus.admin.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-30 17:48.
 * Modify:
 */
@Slf4j
@Service
public class SchemaServiceImpl implements SchemaService {

    @Resource
    private QueryFactory        queryFactory;
    @Resource
    private TableSchemaMapper   tableSchemaMapper;
    @Resource
    private TableColumnMapper   tableColumnMapper;
    @Resource
    private SqlSessionFactory   sqlSessionFactory;
    @Resource
    private UIDGeneratorService uidGeneratorService;
    @Resource
    private DatasourceService   datasourceService;
    @Resource
    private QuartzTaskService   quartzTaskService;
    @Resource
    private RedissonClient      redissonClient;

    /**
     * Update table information.
     *
     * @param ts TableSchemaDO
     * @return int 0 - fail; 1 - success;
     */
    @Override
    public int updateTableSchemaInfo(TableSchemaDO ts) {
        TableSchemaDO oldDo = Preconditions.checkNotNull(
                tableSchemaMapper.findById(ts.getId()),
                I18nMsgConfig.getMessage("ds.schema.id.invalid"));

        TableSchemaDO model = new TableSchemaDO();
        model.setId(ts.getId());
        model.setAlias(ts.getAlias());
        model.setSync(ts.getSync());
        model.setCron(ts.getCron());
        model.setComment(ts.getComment());
        int status = tableSchemaMapper.update(model);
        if (status > 0) {
            if (StringUtils.isNotBlank(oldDo.getCron())) {
                if (StringUtils.isBlank(ts.getCron())) {
                    quartzTaskService.deleteJob2Schedule(tableSchema2QuartzTaskDO(oldDo));
                } else {
                    quartzTaskService.updateJob2Schedule(tableSchema2QuartzTaskDO(oldDo), buildJobDataMap(oldDo));
                }
            } else {
                if (StringUtils.isNotBlank(ts.getCron())) {
                    oldDo.setCron(ts.getCron());
                    quartzTaskService.addJob2Schedule(tableSchema2QuartzTaskDO(oldDo), buildJobDataMap(oldDo));
                }
            }
        }
        return status;
    }

    /**
     * Pulls the specified data source information and saves it.
     *
     * @param dsId Long
     * @return 0 - fail; 1 - success;
     */
    @Override
    public int fetchAndSaveDatabaseInfo(Long dsId) {
        DatasourceInfo dsi = Preconditions.checkNotNull(datasourceService.getDatasourceInfoById(dsId), "The data source does not exist, dsId: " + dsId);

        // Get the database name
        String schema = queryFactory.getSchema(dsi);
        if (StringUtils.isBlank(schema)) {
            throw new BizException(I18nMsgConfig.getMessage("ds.schema.getDatabaseName.fail"));
        }

        RLock lock = redissonClient.getLock("FETCH_TABLE_" + dsId);
        if (lock.isLocked()) {
            throw new BizException(I18nMsgConfig.getMessage("global.get.lock.fail"));
        }

        try {
            lock.lock(5, TimeUnit.MINUTES);

            List<SchemaTable> schemaTableList = queryFactory.allTables(dsi, schema);
            if (CollectionUtils.isEmpty(schemaTableList)) {
                log.info("database name: {}, There is no data table", schema);
                return 1;
            }

            List<String> existTableNames = tableSchemaMapper.getTableNameList(dsId);

            List<TableSchemaDO> needInsertList = new ArrayList<>();
            List<String> allTables = new ArrayList<>();
            for (SchemaTable ti : schemaTableList) {
                String tableName = ti.getName();

                allTables.add(tableName);
                if (existTableNames.contains(tableName)) {
                    continue;
                }

                TableSchemaDO tsdo = new TableSchemaDO();
                tsdo.setSchemaName(schema);
                tsdo.setDatasourceId(dsId);
                tsdo.setName(tableName);
                tsdo.setView(ti.getView());
                if (StringUtils.isNotBlank(ti.getComment())) {
                    tsdo.setComment(ti.getComment());
                }
                needInsertList.add(tsdo);
            }

            // save table info to db.
            batchInsertTableInfo(needInsertList);

            // Deletes a data table that no longer exists.
            existTableNames.removeAll(allTables);
            if (CollectionUtils.isNotEmpty(existTableNames)) {
                tableSchemaMapper.dropTableBy(dsId, existTableNames);
            }

            log.info("Pulls the specified data source information and saves it success!");
            return 1;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    @Transactional
    @Override
    public List<TableColumnDO> fetchAndSaveTableColumnInfo(Long dsId, String tableName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName),
                I18nMsgConfig.getMessage("ds.tableName.null"));
        DatasourceInfo dsi = Preconditions.checkNotNull(
                datasourceService.getDatasourceInfoById(dsId),
                I18nMsgConfig.getMessage("ds.id.invalid"));

        // Get the database name
        String schema = queryFactory.getSchema(dsi);
        if (StringUtils.isBlank(schema)) {
            throw new BizException(I18nMsgConfig.getMessage("ds.schema.getDatabaseName.fail"));
        }

        RLock lock = redissonClient.getLock("FETCH_COLUMN_" + dsId + "_" + tableName);
        if (lock.isLocked()) {
            throw new BizException(I18nMsgConfig.getMessage("global.get.lock.fail"));
        }

        try {
            lock.lock(5, TimeUnit.MINUTES);
            List<SchemaTableField> fieldList = queryFactory.fieldOfTable(dsi, schema, tableName);

            // Get have exist column List
            List<TableColumnDO> existColumnList = tableColumnMapper.getTableColumnList(dsId, tableName);
            Map<String, TableColumnDO> existName2DoMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(existColumnList)) {
                existColumnList.forEach(c -> existName2DoMap.put(c.getName(), c));
            }

            List<TableColumnDO> needInsertList = new ArrayList<>();
            List<TableColumnDO> needUpdateList = new ArrayList<>();

            List<TableColumnDO> retList = new ArrayList<>();
            List<String> allColumns = new ArrayList<>();

            for (SchemaTableField fi : fieldList) {
                String columnName = fi.getName();

                allColumns.add(columnName);
                if (existName2DoMap.containsKey(columnName)) {
                    // old column need update data type.
                    TableColumnDO oldDo = existName2DoMap.get(columnName);
                    if (!StringUtils.equalsIgnoreCase(fi.getDataType().name(), oldDo.getDataType())) {
                        oldDo.setDataType(fi.getDataType().name());
                        needUpdateList.add(oldDo);
                        retList.add(oldDo);
                    }
                    continue;
                }

                // new column
                TableColumnDO tcdo = new TableColumnDO();
                tcdo.setDatasourceId(dsId);
                tcdo.setTableName(tableName);
                tcdo.setName(columnName);
                tcdo.setDataType(fi.getDataType().name());
                tcdo.setClassify(fi.getPrimaryKey() ? 1 : 0);
                tcdo.setHeat(0L);
                tcdo.setStatus(0);
                tcdo.setComment(fi.getComment());

                needInsertList.add(tcdo);

                retList.add(tcdo);

            }

            // save table column info to db.
            batchInsertTableColumnInfo(needInsertList);
            // update table column info to db.
            batchUpdateTableColumnInfo(needUpdateList);

            // Deletes the data table column that no longer exists.
            Set<String> existKeys = existName2DoMap.keySet();
            existKeys.removeAll(allColumns);
            if (CollectionUtils.isNotEmpty(existKeys)) {
                tableColumnMapper.dropColumnBy(dsId, tableName, existKeys);
            }

            log.info("Pulls the specified data source information and saves it success!");
            return retList;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    @Transactional
    @Override
    public List<TableColumnDO> fetchTableColumnList(Long dsId, String tableName) {
        Preconditions.checkNotNull(datasourceService.findById(dsId),
                I18nMsgConfig.getMessage("ds.id.invalid"));
        List<TableColumnDO> list = tableColumnMapper.getTableColumnList(dsId, tableName);
        if (CollectionUtils.isEmpty(list)) {
            list = fetchAndSaveTableColumnInfo(dsId, tableName);
        }
        return list;
    }

    /**
     * @param dsId Long
     * @return List
     */
    @Override
    public List<TableSchemaDO> fetchTableList(Long dsId) {
        Preconditions.checkNotNull(datasourceService.findById(dsId),
                I18nMsgConfig.getMessage("ds.id.invalid"));
        return tableSchemaMapper.list(new TableSchemaDO(dsId));
    }

    /**
     * Modify the table field information.
     *
     * @param tcDo TableColumnDO
     * @return 0 - fail; 1 - success;
     */
    @Transactional
    @Override
    public int changeTableColumnInfo(TableColumnDO tcDo) {
        Preconditions.checkNotNull(tcDo,
                I18nMsgConfig.getMessage("ds.column.info.null"));
        Preconditions.checkArgument(tcDo.getId() != null,
                I18nMsgConfig.getMessage("ds.column.id.null"));
        return tableColumnMapper.update(tcDo);
    }

    /**
     * Initializes the synchronous Job
     */
    @Override
    public void initSyncJob() {
        List<TableSchemaDO> tsList = tableSchemaMapper.getNeedSyncTableList();
        if (CollectionUtils.isEmpty(tsList)) {
            return;
        }

        for (TableSchemaDO ts : tsList) {
            quartzTaskService.addJob2Schedule(tableSchema2QuartzTaskDO(ts), buildJobDataMap(ts));
        }
    }

    /**
     * build the job data map.
     *
     * @param ts TableSchemaDO
     * @return Map
     */
    private Map<String, Object> buildJobDataMap(TableSchemaDO ts) {
        Map<String, Object> jobDataMap = new HashMap<>();
        jobDataMap.put("dsId", ts.getDatasourceId());
        jobDataMap.put("tableName", ts.getName());
        return jobDataMap;
    }


    /**
     * build the task object.
     *
     * @param ts TableSchemaDO
     * @return QuartzTaskDO
     */
    private QuartzTaskDO tableSchema2QuartzTaskDO(TableSchemaDO ts) {
        QuartzTaskDO qt = new QuartzTaskDO();
        qt.setTaskName(ts.getDatasourceId() + "_" + ts.getName());
        qt.setCron(ts.getCron());
        qt.setGroupName("TABLE_SCHEMA_GROUP");
        qt.setStatus(1);
        qt.setJobClass(SchemaTableSyncJob.class.getName());

        return qt;
    }

    /**
     * Modify the table heat value.
     *
     * @param dsId      Long
     * @param tableName String
     * @param incHeat   Integer
     * @return int 0 - fail; 1 - success;
     */
    @Override
    public int updateTableHeat(Long dsId, String tableName, Integer incHeat) {
        if (StringUtils.isBlank(tableName) || dsId == null || incHeat == null) {
            return 0;
        }

        return tableSchemaMapper.updateTableHeatValue(dsId, tableName, incHeat);
    }

    /**
     * Batch increment the heat value of the table field.
     *
     * @param dsId      Long
     * @param tableName String
     * @param columns   List
     * @return int 0 - fail; 1 - success;
     */
    @Override
    public int incColumnHeat(Long dsId, String tableName, List<String> columns) {
        if (CollectionUtils.isEmpty(columns) || StringUtils.isBlank(tableName) || dsId == null) {
            return 0;
        }

        return tableColumnMapper.updateTableColumnHeatValue(dsId, tableName, columns);
    }

    /**
     * Batch insert table column information
     *
     * @param columnDOs List
     */
    private void batchInsertTableColumnInfo(List<TableColumnDO> columnDOs) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            TableColumnMapper mapper = session.getMapper(TableColumnMapper.class);
            for (TableColumnDO stdo : columnDOs) {
                stdo.setId(uidGeneratorService.getUid());
                mapper.insert(stdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("Batch insert table column information exception", e);
            session.rollback();
            throw new BizException(I18nMsgConfig.getMessage("ds.batch.insert.column.error"));
        } finally {
            log.debug("Batch insert table column information: {} rows.", count);
            session.close();
        }
    }

    /**
     * Batch update table column information
     *
     * @param columnDOs List
     */
    private void batchUpdateTableColumnInfo(List<TableColumnDO> columnDOs) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            TableColumnMapper mapper = session.getMapper(TableColumnMapper.class);
            for (TableColumnDO stdo : columnDOs) {
                mapper.update(stdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("Batch update table column information exception", e);
            session.rollback();
            throw new BizException(I18nMsgConfig.getMessage("ds.batch.update.column.error"));
        } finally {
            log.debug("Batch update table column information: {} rows.", count);
            session.close();
        }
    }

    /**
     * Batch insert table information
     *
     * @param tableDOs List
     */
    private void batchInsertTableInfo(List<TableSchemaDO> tableDOs) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        int count = 0;
        try {
            TableSchemaMapper mapper = session.getMapper(TableSchemaMapper.class);
            for (TableSchemaDO stdo : tableDOs) {
                stdo.setId(uidGeneratorService.getUid());
                mapper.insert(stdo);
                count++;
            }
            session.commit();
        } catch (Exception e) {
            log.error("Batch insert data table information exception", e);
            session.rollback();
            throw new BizException(I18nMsgConfig.getMessage("ds.batch.insert.table.error"));
        } finally {
            log.debug("Batch insert data table information: {} rows.", count);
            session.close();
        }
    }
}

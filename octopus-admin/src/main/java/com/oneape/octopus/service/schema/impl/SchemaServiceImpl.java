package com.oneape.octopus.service.schema.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.datasource.QueryFactory;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;
import com.oneape.octopus.mapper.schema.TableColumnMapper;
import com.oneape.octopus.mapper.schema.TableSchemaMapper;
import com.oneape.octopus.model.domain.schema.TableColumnDO;
import com.oneape.octopus.model.domain.schema.TableSchemaDO;
import com.oneape.octopus.service.schema.DatasourceService;
import com.oneape.octopus.service.schema.SchemaService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Pulls the specified data source information and saves it.
     *
     * @param dsId Long
     * @return 0 - fail; 1 - success;
     */
    @Override
    public int fetchAndSaveDatabaseInfo(Long dsId) {
        DatasourceInfo dsi = Preconditions.checkNotNull(datasourceService.getDatasourceInfoById(dsId), "The data source does not exist。 dsId: " + dsId);

        // Get the database name
        String schema = queryFactory.getSchema(dsi);
        if (StringUtils.isBlank(schema)) {
            throw new BizException("Failed to get database name~");
        }

        List<TableInfo> tableInfoList = queryFactory.allTables(dsi, schema);
        if (CollectionUtils.isEmpty(tableInfoList)) {
            log.info("database name: {}, There is no data table", schema);
            return 1;
        }

        List<String> existTableNames = tableSchemaMapper.getTableNameList(dsId);

        List<TableSchemaDO> needInsertList = new ArrayList<>();
        List<String> allTables = new ArrayList<>();
        for (TableInfo ti : tableInfoList) {
            String tableName = ti.getName();

            allTables.add(tableName);
            if (existTableNames.contains(tableName)) {
                continue;
            }

            TableSchemaDO tsdo = new TableSchemaDO();
            tsdo.setDatasourceId(dsId);
            tsdo.setName(tableName);
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
    }

    /**
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    @Override
    public int fetchAndSaveTableColumnInfo(Long dsId, String tableName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName), "The table name is null.");
        DatasourceInfo dsi = Preconditions.checkNotNull(datasourceService.getDatasourceInfoById(dsId), "The data source does not exist。 dsId: " + dsId);

        // Get the database name
        String schema = queryFactory.getSchema(dsi);
        if (StringUtils.isBlank(schema)) {
            throw new BizException("Failed to get database name~");
        }

        List<FieldInfo> fieldList = queryFactory.fieldOfTable(dsi, schema, tableName);
        List<String> existColumnNames = tableColumnMapper.getTableColumnNameList(dsId, tableName);

        List<TableColumnDO> needInsertList = new ArrayList<>();
        List<String> allColumns = new ArrayList<>();
        for (FieldInfo fi : fieldList) {
            String columnName = fi.getName();

            allColumns.add(columnName);
            if (existColumnNames.contains(columnName)) {
                continue;
            }

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
        }

        // save table column info to db.
        batchInsertTableColumnInfo(needInsertList);

        // Deletes the data table column that no longer exists.
        existColumnNames.removeAll(allColumns);
        if (CollectionUtils.isNotEmpty(existColumnNames)) {
            tableColumnMapper.dropColumnBy(dsId, tableName, existColumnNames);
        }

        log.info("Pulls the specified data source information and saves it success!");
        return 1;
    }

    /**
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    @Override
    public List<TableColumnDO> fetchTableColumnList(Long dsId, String tableName) {
        Preconditions.checkNotNull(datasourceService.findById(dsId), "The data source does not exist");
        return tableColumnMapper.getTableColumnList(dsId, tableName);
    }

    /**
     * @param dsId Long
     * @return List
     */
    @Override
    public List<TableSchemaDO> fetchTableList(Long dsId) {
        Preconditions.checkNotNull(datasourceService.findById(dsId), "The data source does not exist");
        return tableSchemaMapper.list(new TableSchemaDO(dsId));
    }

    /**
     * Modify the table field information.
     *
     * @param tcDo TableColumnDO
     * @return 0 - fail; 1 - success;
     */
    @Override
    public int changeTableColumnInfo(TableColumnDO tcDo) {
        Preconditions.checkNotNull(tcDo, "The column object is null.");
        Preconditions.checkArgument(tcDo.getId() != null, "The  primary key of column is null.");
        return tableColumnMapper.update(tcDo);
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
            throw new BizException("Batch insert table column information exception");
        } finally {
            log.debug("Batch insert table column information: {} rows.", count);
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
            throw new BizException("Batch insert data table information exception");
        } finally {
            log.debug("Batch insert data table information: {} rows.", count);
            session.close();
        }
    }
}

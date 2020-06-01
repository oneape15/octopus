package com.oneape.octopus.service.schema.impl;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.BizException;
import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.datasource.QueryFactory;
import com.oneape.octopus.datasource.schema.TableInfo;
import com.oneape.octopus.mapper.schema.TableColumnMapper;
import com.oneape.octopus.mapper.schema.TableSchemaMapper;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import com.oneape.octopus.model.DO.schema.TableColumnDO;
import com.oneape.octopus.model.DO.schema.TableSchemaDO;
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

    /**
     * Pulls the specified data source information and saves it.
     *
     * @param ddo DatasourceDO
     * @return 0 - fail; 1 - success;
     */
    @Override
    public int fetchAndSaveDatabaseInfo(DatasourceDO ddo) {
        Preconditions.checkNotNull(ddo, "data source info is NULL");
        DatasourceInfo dsi = new DatasourceInfo();
        dsi.setId(ddo.getId());
        dsi.setDatasourceType(DatasourceTypeHelper.byName(ddo.getType()));
        dsi.setUsername(ddo.getUsername());
        dsi.setPassword(ddo.getPassword());
        dsi.setUrl(ddo.getJdbcUrl());

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

        List<String> existTableNames = tableSchemaMapper.getTableNameList(ddo.getId());

        List<TableSchemaDO> needInsertList = new ArrayList<>();
        List<String> allTables = new ArrayList<>();
        for (TableInfo ti : tableInfoList) {
            String tableName = ti.getName();

            allTables.add(tableName);
            if (existTableNames.contains(tableName)) {
                continue;
            }

            TableSchemaDO tsdo = new TableSchemaDO();
            tsdo.setDatasourceId(ddo.getId());
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
            tableSchemaMapper.deleteBy(ddo.getId(), existTableNames);
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
        return tableColumnMapper.getTableColumnList(dsId, tableName);
    }

    /**
     * @param dsId Long
     * @return List
     */
    @Override
    public List<TableSchemaDO> fetchTableList(Long dsId) {
        return tableSchemaMapper.list(new TableSchemaDO(dsId));
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
        } finally {
            log.debug("Batch insert data table information: {} rows.", count);
            session.close();
        }
    }
}

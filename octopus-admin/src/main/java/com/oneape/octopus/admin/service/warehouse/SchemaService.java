package com.oneape.octopus.admin.service.warehouse;

import com.oneape.octopus.domain.warehouse.TableColumnDO;
import com.oneape.octopus.domain.warehouse.TableSchemaDO;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-30 14:40.
 * Modify:
 */
public interface SchemaService {

    /**
     * Update table information.
     *
     * @param ts TableSchemaDO
     * @return int 0 - fail; 1 - success;
     */
    int updateTableSchemaInfo(TableSchemaDO ts);

    /**
     * Pulls the specified data source information and saves it.
     *
     * @param dsId Long
     * @return 0 - fail; 1 - success;
     */
    int fetchAndSaveDatabaseInfo(Long dsId);

    /**
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    List<TableColumnDO> fetchAndSaveTableColumnInfo(Long dsId, String tableName);

    /**
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    List<TableColumnDO> fetchTableColumnList(Long dsId, String tableName);

    /**
     * @param dsId Long
     * @return List
     */
    List<TableSchemaDO> fetchTableList(Long dsId);

    /**
     * Modify the table field information.
     *
     * @param tcDo TableColumnDO
     * @return 0 - fail; 1 - success;
     */
    int changeTableColumnInfo(TableColumnDO tcDo);

    /**
     * Initializes the synchronous Job
     */
    void initSyncJob();

    /**
     * Modify the table heat value.
     *
     * @param dsId      Long
     * @param tableName String
     * @param incHeat   Integer
     * @return int 0 - fail; 1 - success;
     */
    int updateTableHeat(Long dsId, String tableName, Integer incHeat);

    /**
     * Batch increment the heat value of the table field.
     *
     * @param dsId      Long
     * @param tableName String
     * @param columns   List
     * @return int 0 - fail; 1 - success;
     */
    int incColumnHeat(Long dsId, String tableName, List<String> columns);
}

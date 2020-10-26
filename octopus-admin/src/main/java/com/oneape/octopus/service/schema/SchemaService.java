package com.oneape.octopus.service.schema;

import com.oneape.octopus.model.domain.schema.TableColumnDO;
import com.oneape.octopus.model.domain.schema.TableSchemaDO;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-30 14:40.
 * Modify:
 */
public interface SchemaService {

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
    int fetchAndSaveTableColumnInfo(Long dsId, String tableName);

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

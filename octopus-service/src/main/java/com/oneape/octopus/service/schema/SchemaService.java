package com.oneape.octopus.service.schema;

import com.oneape.octopus.model.DO.schema.DatasourceDO;
import com.oneape.octopus.model.DO.schema.TableColumnDO;
import com.oneape.octopus.model.DO.schema.TableSchemaDO;

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
     * @param ddo DatasourceDO
     * @return 0 - fail; 1 - success;
     */
    int fetchAndSaveDatabaseInfo(DatasourceDO ddo);

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

}

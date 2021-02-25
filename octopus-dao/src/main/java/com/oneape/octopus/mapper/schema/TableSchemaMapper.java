package com.oneape.octopus.mapper.schema;

import com.oneape.octopus.domain.schema.TableSchemaDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.schema.provider.TableSchemaSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-30 18:11.
 * Modify:
 */
@Mapper
public interface TableSchemaMapper {

    /**
     * append new data.
     *
     * @param model T
     * @return int 1 - success； 0 - fail
     */
    @InsertProvider(type = TableSchemaSqlProvider.class, method = "insert")
    int insert(TableSchemaDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = TableSchemaSqlProvider.class, method = "updateById")
    int update(TableSchemaDO model);

    /**
     * get data by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = TableSchemaSqlProvider.class, method = "findById")
    TableSchemaDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = TableSchemaSqlProvider.class, method = "list")
    List<TableSchemaDO> list(@Param("model") TableSchemaDO model);


    @Select({
            "SELECT name FROM " + TableSchemaSqlProvider.TABLE_NAME
                    + " WHERE  " + BaseSqlProvider.FIELD_ARCHIVE + " = 0  AND datasource_id = #{dsId}"
    })
    List<String> getTableNameList(@Param("dsId") Long dsId);

    /**
     * @param dsId       Long
     * @param tableNames List
     * @return int
     */
    @UpdateProvider(type = TableSchemaSqlProvider.class, method = "dropTableBy")
    int dropTableBy(@Param("dsId") Long dsId, @Param("tableNames") List<String> tableNames);

    /**
     * @param dsId Long
     * @return List
     */
    @Select({
            "SELECT * FROM " + TableSchemaSqlProvider.TABLE_NAME
                    + " WHERE  " + BaseSqlProvider.FIELD_ARCHIVE + " = 0  AND datasource_id = #{dsId}"
    })
    List<TableSchemaDO> getTableList(@Param("dsId") Long dsId);

    /**
     * Query table information according to dsId and tableName
     *
     * @param dsId      Long
     * @param tableName String
     * @return TableSchemaDO
     */
    @Select({
            "SELECT * FROM " + TableSchemaSqlProvider.TABLE_NAME
                    + " WHERE  " + BaseSqlProvider.FIELD_ARCHIVE + " = 0  AND datasource_id = #{dsId} AND name = #{tableName}"
    })
    TableSchemaDO findBy(@Param("dsId") Long dsId, @Param("tableName") String tableName);

    /**
     * Modify the table heat value.
     */
    @Update({
            "UPDATE " + TableSchemaSqlProvider.TABLE_NAME +
                    " SET heat = heat + #{incHeat} " +
                    " WHERE " + BaseSqlProvider.FIELD_ARCHIVE + " = 0  AND datasource_id = #{dsId} AND name = #{tableName}"
    })
    int updateTableHeatValue(@Param("dsId") Long dsId, @Param("tableName") String tableName, @Param("incHeat") Integer incHeat);

    @Select({
            "SELECT * FROM " + TableSchemaSqlProvider.TABLE_NAME
                    + " WHERE " + BaseSqlProvider.FIELD_ARCHIVE + " = 0 AND sync = 1 AND cron IS NOT NULL"
    })
    List<TableSchemaDO> getNeedSyncTableList();
}

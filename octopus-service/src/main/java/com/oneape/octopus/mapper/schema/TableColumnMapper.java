package com.oneape.octopus.mapper.schema;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.schema.provider.TableColumnSqlProvider;
import com.oneape.octopus.mapper.schema.provider.TableSchemaSqlProvider;
import com.oneape.octopus.model.DO.schema.TableColumnDO;
import com.oneape.octopus.model.DO.schema.TableSchemaDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-30 18:11.
 * Modify:
 */
@Mapper
public interface TableColumnMapper {

    /**
     * append new data.
     *
     * @param model T
     * @return int 1 - success； 0 - fail
     */
    @InsertProvider(type = TableColumnSqlProvider.class, method = "insert")
    int insert(TableColumnDO model);

    /**
     * get data by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = TableColumnSqlProvider.class, method = "findById")
    TableColumnDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = TableColumnSqlProvider.class, method = "list")
    List<TableColumnDO> list(@Param("model") TableColumnDO model);


    @Select({
            "SELECT name FROM " + TableColumnSqlProvider.TABLE_NAME
                    + " WHERE  " + BaseSqlProvider.FIELD_ARCHIVE + " = 0  AND datasource_id = #{dsId} AND table_name = #{tableName}"
    })
    List<String> getTableColumnNameList(@Param("dsId") Long dsId, @Param("tableName") String tableName);

    /**
     * @param dsId      Long
     * @param tableName String
     * @param columns   List
     * @return int
     */
    @UpdateProvider(type = TableColumnSqlProvider.class, method = "deleteBy")
    int deleteBy(@Param("dsId") Long dsId, @Param("tableName") String tableName, @Param("columns") List<String> columns);

    /**
     * @param dsId      Long
     * @param tableName String
     * @return List
     */
    @Select({
            "SELECT * FROM " + TableColumnSqlProvider.TABLE_NAME
                    + " WHERE  " + BaseSqlProvider.FIELD_ARCHIVE + " = 0  AND datasource_id = #{dsId} AND table_name = #{tableName}"
    })
    List<TableColumnDO> getTableColumnList(@Param("dsId") Long dsId, @Param("tableName") String tableName);
}

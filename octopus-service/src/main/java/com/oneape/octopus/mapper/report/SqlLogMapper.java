package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.SqlLogSqlProvider;
import com.oneape.octopus.model.DO.report.SqlLogDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SqlLogMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = SqlLogSqlProvider.class, method = "insert")
    int insert(SqlLogDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = SqlLogSqlProvider.class, method = "updateById")
    int update(SqlLogDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = SqlLogSqlProvider.class, method = "deleteById")
    int delete(SqlLogDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = SqlLogSqlProvider.class, method = "findById")
    SqlLogDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = SqlLogSqlProvider.class, method = "list")
    List<SqlLogDO> list(@Param("model") SqlLogDO model);
}

package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportSqlSqlProvider;
import com.oneape.octopus.model.DO.report.ReportSqlDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportSqlMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ReportSqlSqlProvider.class, method = "insert")
    int insert(ReportSqlDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportSqlSqlProvider.class, method = "updateById")
    int update(ReportSqlDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportSqlSqlProvider.class, method = "deleteById")
    int delete(ReportSqlDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportSqlSqlProvider.class, method = "findById")
    ReportSqlDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportSqlSqlProvider.class, method = "list")
    List<ReportSqlDO> list(@Param("model") ReportSqlDO model);
}

package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportSqlProvider;
import com.oneape.octopus.model.DO.report.ReportDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * The report Mapper.
 */
@Mapper
public interface ReportMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ReportSqlProvider.class, method = "insert")
    int insert(ReportDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportSqlProvider.class, method = "updateById")
    int update(ReportDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportSqlProvider.class, method = "deleteById")
    int delete(ReportDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportSqlProvider.class, method = "findById")
    ReportDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportSqlProvider.class, method = "list")
    List<ReportDO> list(@Param("model") ReportDO model);
}

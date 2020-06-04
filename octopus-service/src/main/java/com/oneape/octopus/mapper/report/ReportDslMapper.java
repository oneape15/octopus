package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportDslSqlProvider;
import com.oneape.octopus.model.DO.report.ReportDslDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportDslMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ReportDslSqlProvider.class, method = "insert")
    int insert(ReportDslDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportDslSqlProvider.class, method = "updateById")
    int update(ReportDslDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportDslSqlProvider.class, method = "deleteById")
    int delete(ReportDslDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportDslSqlProvider.class, method = "findById")
    ReportDslDO findById(@Param("id") Long id);

    /**
     * Deleted data base on report Id.
     *
     * @param reportId Long
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportDslSqlProvider.class, method = "deleteByReportId")
    int deleteByReportId(@Param("reportId") Long reportId);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportDslSqlProvider.class, method = "list")
    List<ReportDslDO> list(@Param("model") ReportDslDO model);

    @SelectProvider(type = ReportDslSqlProvider.class, method = "findByReportId")
    ReportDslDO findByReportId(@Param("reportId") Long reportId);
}

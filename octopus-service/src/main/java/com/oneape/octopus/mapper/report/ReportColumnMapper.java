package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportColumnSqlProvider;
import com.oneape.octopus.model.DO.report.ReportColumnDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportColumnMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ReportColumnSqlProvider.class, method = "insert")
    int insert(ReportColumnDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportColumnSqlProvider.class, method = "updateById")
    int update(ReportColumnDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportColumnSqlProvider.class, method = "deleteById")
    int delete(ReportColumnDO model);

    /**
     * Deleted data base on report Id.
     *
     * @param reportId Long
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportColumnSqlProvider.class, method = "deleteByReportId")
    int deleteByReportId(@Param("reportId") Long reportId);

    /**
     * Remove unwanted columns
     *
     * @param reportId Long
     * @param names    List
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportColumnSqlProvider.class, method = "deleteByNames")
    int deleteByNames(@Param("reportId") Long reportId, @Param("names") List<String> names);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportColumnSqlProvider.class, method = "findById")
    ReportColumnDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportColumnSqlProvider.class, method = "list")
    List<ReportColumnDO> list(@Param("model") ReportColumnDO model);
}

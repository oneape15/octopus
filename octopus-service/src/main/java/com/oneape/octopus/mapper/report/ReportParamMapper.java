package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportParamSqlProvider;
import com.oneape.octopus.model.DO.report.ReportParamDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportParamMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ReportParamSqlProvider.class, method = "insert")
    int insert(ReportParamDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportParamSqlProvider.class, method = "updateById")
    int update(ReportParamDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportParamSqlProvider.class, method = "deleteById")
    int delete(ReportParamDO model);

    /**
     * Deleted data base on Id.
     *
     * @param reportId Long
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportParamSqlProvider.class, method = "deleteByReportId")
    int deleteByReportId(@Param("reportId") Long reportId);

    /**
     * Remove unwanted parameters
     *
     * @param reportId Long
     * @param names    List
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ReportParamSqlProvider.class, method = "deleteByNames")
    int deleteByNames(@Param("reportId") Long reportId, @Param("names") List<String> names);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportParamSqlProvider.class, method = "findById")
    ReportParamDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportParamSqlProvider.class, method = "list")
    List<ReportParamDO> list(@Param("model") ReportParamDO model);

    @SelectProvider(type = ReportParamSqlProvider.class, method = "findByReportId")
    List<ReportParamDO> findByReportId(@Param("reportId") Long reportId);
}

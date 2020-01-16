package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportColumnSqlProvider;
import com.oneape.octopus.model.DO.report.ReportColumnDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportColumnMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ReportColumnSqlProvider.class, method = "insert")
    int insert(ReportColumnDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportColumnSqlProvider.class, method = "updateById")
    int update(ReportColumnDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportColumnSqlProvider.class, method = "deleteById")
    int delete(ReportColumnDO model);

    /**
     * 根据报表Id删除
     *
     * @param reportId Long
     * @return int 1 - 成功；0 - 失败
     */
    @UpdateProvider(type = ReportColumnSqlProvider.class, method = "deleteByReportId")
    int deleteByReportId(@Param("reportId") Long reportId);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportColumnSqlProvider.class, method = "findById")
    ReportColumnDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportColumnSqlProvider.class, method = "list")
    List<ReportColumnDO> list(@Param("model") ReportColumnDO model);
}

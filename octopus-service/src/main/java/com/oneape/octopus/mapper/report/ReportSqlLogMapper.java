package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportSqlLogSqlProvider;
import com.oneape.octopus.model.DO.report.ReportSqlLogDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportSqlLogMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ReportSqlLogSqlProvider.class, method = "insert")
    int insert(ReportSqlLogDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportSqlLogSqlProvider.class, method = "updateById")
    int update(ReportSqlLogDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportSqlLogSqlProvider.class, method = "deleteById")
    int delete(ReportSqlLogDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportSqlLogSqlProvider.class, method = "findById")
    ReportSqlLogDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportSqlLogSqlProvider.class, method = "list")
    List<ReportSqlLogDO> list(@Param("model") ReportSqlLogDO model);
}

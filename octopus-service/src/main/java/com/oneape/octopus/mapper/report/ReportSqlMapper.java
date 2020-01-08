package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportSqlSqlProvider;
import com.oneape.octopus.model.DO.report.ReportSqlDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportSqlMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ReportSqlSqlProvider.class, method = "insert")
    int insert(ReportSqlDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportSqlSqlProvider.class, method = "updateById")
    int update(ReportSqlDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportSqlSqlProvider.class, method = "deleteById")
    int delete(ReportSqlDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportSqlSqlProvider.class, method = "findById")
    ReportSqlDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportSqlSqlProvider.class, method = "list")
    List<ReportSqlDO> list(@Param("model") ReportSqlDO model);
}

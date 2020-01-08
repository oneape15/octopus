package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.ReportGroupSqlProvider;
import com.oneape.octopus.model.DO.report.ReportGroupDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 报表组Mapper
 */
@Mapper
public interface ReportGroupMapper {

    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ReportGroupSqlProvider.class, method = "insert")
    int insert(ReportGroupDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportGroupSqlProvider.class, method = "updateById")
    int update(ReportGroupDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ReportGroupSqlProvider.class, method = "deleteById")
    int delete(ReportGroupDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ReportGroupSqlProvider.class, method = "findById")
    ReportGroupDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ReportGroupSqlProvider.class, method = "list")
    List<ReportGroupDO> list(@Param("model") ReportGroupDO model);
}

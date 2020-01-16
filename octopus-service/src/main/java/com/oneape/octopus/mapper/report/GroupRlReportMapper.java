package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.GroupRlReportSqlProvider;
import com.oneape.octopus.model.DO.report.GroupRlReportDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupRlReportMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = GroupRlReportSqlProvider.class, method = "insert")
    int insert(GroupRlReportDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = GroupRlReportSqlProvider.class, method = "updateById")
    int update(GroupRlReportDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = GroupRlReportSqlProvider.class, method = "deleteById")
    int delete(GroupRlReportDO model);
    /**
     * 根据报表Id删除
     *
     * @param reportId Long
     * @return int 1 - 成功；0 - 失败
     */
    @UpdateProvider(type = GroupRlReportSqlProvider.class, method = "deleteByReportId")
    int deleteByReportId(@Param("reportId") Long reportId);


    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = GroupRlReportSqlProvider.class, method = "findById")
    GroupRlReportDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = GroupRlReportSqlProvider.class, method = "list")
    List<GroupRlReportDO> list(@Param("model") GroupRlReportDO model);

    /**
     * 删除指定报表的报表组关联关系
     *
     * @param reportId Long
     * @param groupIds List
     * @return int
     */
    @DeleteProvider(type = GroupRlReportSqlProvider.class, method = "deleteBy")
    int deleteBy(@Param("reportId") Long reportId, @Param("groupIds") List<Long> groupIds);
}

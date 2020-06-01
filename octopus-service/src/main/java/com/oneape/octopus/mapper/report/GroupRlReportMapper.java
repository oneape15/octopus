package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.GroupRlReportSqlProvider;
import com.oneape.octopus.model.DO.report.GroupRlReportDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupRlReportMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = GroupRlReportSqlProvider.class, method = "insert")
    int insert(GroupRlReportDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = GroupRlReportSqlProvider.class, method = "updateById")
    int update(GroupRlReportDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
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
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = GroupRlReportSqlProvider.class, method = "findById")
    GroupRlReportDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
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

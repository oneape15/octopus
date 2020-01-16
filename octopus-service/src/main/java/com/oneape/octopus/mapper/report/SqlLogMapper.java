package com.oneape.octopus.mapper.report;

import com.oneape.octopus.mapper.report.provider.SqlLogSqlProvider;
import com.oneape.octopus.model.DO.report.SqlLogDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SqlLogMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = SqlLogSqlProvider.class, method = "insert")
    int insert(SqlLogDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = SqlLogSqlProvider.class, method = "updateById")
    int update(SqlLogDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = SqlLogSqlProvider.class, method = "deleteById")
    int delete(SqlLogDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = SqlLogSqlProvider.class, method = "findById")
    SqlLogDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = SqlLogSqlProvider.class, method = "list")
    List<SqlLogDO> list(@Param("model") SqlLogDO model);
}

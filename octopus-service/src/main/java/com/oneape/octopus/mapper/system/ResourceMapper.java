package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.ResourceSqlProvider;
import com.oneape.octopus.model.DO.system.ResourceDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResourceMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = ResourceSqlProvider.class, method = "insert")
    int insert(ResourceDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ResourceSqlProvider.class, method = "updateById")
    int update(ResourceDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = ResourceSqlProvider.class, method = "deleteById")
    int delete(ResourceDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ResourceSqlProvider.class, method = "findById")
    ResourceDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ResourceSqlProvider.class, method = "list")
    List<ResourceDO> list(@Param("model") ResourceDO model);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ResourceSqlProvider.class, method = "listWithOrder")
    List<ResourceDO> listWithOrder(@Param("model") ResourceDO model, @Param("orderFields") List<String> orderFields);
}

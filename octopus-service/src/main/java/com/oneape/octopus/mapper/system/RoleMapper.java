package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.RoleSqlProvider;
import com.oneape.octopus.model.DO.system.RoleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = RoleSqlProvider.class, method = "insert")
    int insert(RoleDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = RoleSqlProvider.class, method = "updateById")
    int update(RoleDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = RoleSqlProvider.class, method = "deleteById")
    int delete(RoleDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "findById")
    RoleDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "list")
    List<RoleDO> list(@Param("model") RoleDO model);

    @SelectProvider(type = RoleSqlProvider.class, method = "listOrLink")
    List<RoleDO> listOrLink(@Param("model") RoleDO model);

    /**
     * 根据实体中不为null的属性通过"OR"关键字关联起来进行查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "listWithOrder")
    List<RoleDO> listWithOrder(@Param("model") RoleDO model, @Param("orderFields") List<String> orderFields);
}

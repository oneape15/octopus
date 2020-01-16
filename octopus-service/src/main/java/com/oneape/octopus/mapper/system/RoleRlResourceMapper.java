package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.RoleRlResourceSqlProvider;
import com.oneape.octopus.model.DO.system.RoleRlResourceDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleRlResourceMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = RoleRlResourceSqlProvider.class, method = "insert")
    int insert(RoleRlResourceDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = RoleRlResourceSqlProvider.class, method = "updateById")
    int update(RoleRlResourceDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = RoleRlResourceSqlProvider.class, method = "delete")
    int delete(RoleRlResourceDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = RoleRlResourceSqlProvider.class, method = "findById")
    RoleRlResourceDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleRlResourceSqlProvider.class, method = "list")
    List<RoleRlResourceDO> list(@Param("model") RoleRlResourceDO model);

    /**
     * 根据角色Id列表，获取对应的资源id信息
     *
     * @param roleIds List
     * @return List
     */
    @SelectProvider(type = RoleRlResourceSqlProvider.class, method = "getResIdByRoleIds")
    List<RoleRlResourceDO> getResIdByRoleIds(@Param("roleIds") List<Long> roleIds);
}

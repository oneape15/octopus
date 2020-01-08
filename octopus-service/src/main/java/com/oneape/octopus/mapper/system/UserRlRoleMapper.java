package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.UserRlRoleSqlProvider;
import com.oneape.octopus.model.DO.system.UserRlRoleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRlRoleMapper {
    /**
     * 新增数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @InsertProvider(type = UserRlRoleSqlProvider.class, method = "insert")
    int insert(UserRlRoleDO model);

    /**
     * 通过主键更新数据
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = UserRlRoleSqlProvider.class, method = "updateById")
    int update(UserRlRoleDO model);

    /**
     * 通过主键删除数据（软删除，更新archive状态)
     *
     * @param model T
     * @return int 1 - 成功； 0 - 失败
     */
    @UpdateProvider(type = UserRlRoleSqlProvider.class, method = "deleteById")
    int delete(UserRlRoleDO model);

    /**
     * 通过主键查找
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = UserRlRoleSqlProvider.class, method = "findById")
    UserRlRoleDO findById(@Param("id") Long id);

    /**
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = UserRlRoleSqlProvider.class, method = "list")
    List<UserRlRoleDO> list(@Param("model") UserRlRoleDO model);
}

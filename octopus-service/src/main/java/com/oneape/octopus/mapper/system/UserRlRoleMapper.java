package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.UserRlRoleSqlProvider;
import com.oneape.octopus.model.DO.system.UserRlRoleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRlRoleMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = UserRlRoleSqlProvider.class, method = "insert")
    int insert(UserRlRoleDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserRlRoleSqlProvider.class, method = "updateById")
    int update(UserRlRoleDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserRlRoleSqlProvider.class, method = "deleteById")
    int delete(UserRlRoleDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = UserRlRoleSqlProvider.class, method = "findById")
    UserRlRoleDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = UserRlRoleSqlProvider.class, method = "list")
    List<UserRlRoleDO> list(@Param("model") UserRlRoleDO model);
}

package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.RoleSqlProvider;
import com.oneape.octopus.model.domain.system.RoleDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = RoleSqlProvider.class, method = "insert")
    int insert(RoleDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleSqlProvider.class, method = "updateById")
    int update(RoleDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = RoleSqlProvider.class, method = "deleteById")
    int delete(RoleDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "findById")
    RoleDO findById(@Param("id") Long id);

    @SelectProvider(type = RoleSqlProvider.class, method = "findRoleByUserId")
    List<RoleDO> findRoleByUserId(@Param("userId") Long userId);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "list")
    List<RoleDO> list(@Param("model") RoleDO model);

    @SelectProvider(type = RoleSqlProvider.class, method = "listOrLink")
    List<RoleDO> listOrLink(@Param("model") RoleDO model);

    @SelectProvider(type = RoleSqlProvider.class, method = "getSameNameOrCodeRole")
    int getSameNameOrCodeRole(@Param("name") String name, @Param("code") String code, @Param("filterId") Long filterId);

    /**
     * The query is associated with the "OR" keyword based on attributes that are not null in the entity.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "listWithOrder")
    List<RoleDO> listWithOrder(@Param("model") RoleDO model, @Param("orderFields") List<String> orderFields);
}

package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.RoleSqlProvider;
import com.oneape.octopus.model.DO.system.RoleDO;
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

    /**
     * 根据实体中不为null的属性通过"OR"关键字关联起来进行查询
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = RoleSqlProvider.class, method = "listWithOrder")
    List<RoleDO> listWithOrder(@Param("model") RoleDO model, @Param("orderFields") List<String> orderFields);
}

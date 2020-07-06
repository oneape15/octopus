package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.ResourceSqlProvider;
import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.DTO.system.ResourceDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ResourceMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = ResourceSqlProvider.class, method = "insert")
    int insert(ResourceDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ResourceSqlProvider.class, method = "updateById")
    int update(ResourceDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = ResourceSqlProvider.class, method = "deleteById")
    int delete(ResourceDO model);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = ResourceSqlProvider.class, method = "findById")
    ResourceDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ResourceSqlProvider.class, method = "list")
    List<ResourceDO> list(@Param("model") ResourceDO model);

    @SelectProvider(type = ResourceSqlProvider.class, method = "listByRoleIds")
    List<ResourceDTO> listByRoleIds(@Param("roleIds") List<Long> roleIds);


    @SelectProvider(type = ResourceSqlProvider.class, method = "getSameBy")
    int getSameBy(@Param("parentId") Long parentId, @Param("name") String name, @Param("filterId") Long filterId);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = ResourceSqlProvider.class, method = "listWithOrder")
    List<ResourceDO> listWithOrder(@Param("model") ResourceDO model, @Param("orderFields") List<String> orderFields);
}

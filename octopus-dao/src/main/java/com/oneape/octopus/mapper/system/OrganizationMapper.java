package com.oneape.octopus.mapper.system;

import com.oneape.octopus.domain.system.OrganizationDO;
import com.oneape.octopus.mapper.system.provider.OrganizationSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 14:38.
 * Modify:
 */
public interface OrganizationMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = OrganizationSqlProvider.class, method = "insert")
    int insert(OrganizationDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = OrganizationSqlProvider.class, method = "updateById")
    int update(OrganizationDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = OrganizationSqlProvider.class, method = "deleteById")
    int deleteById(OrganizationDO model);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = OrganizationSqlProvider.class, method = "list")
    List<OrganizationDO> list(@Param("model") OrganizationDO model);

    /**
     * @param id Long
     * @return OrganizationDO
     */
    @SelectProvider(type = OrganizationSqlProvider.class, method = "findById")
    OrganizationDO findById(@Param("id") Long id);

    /**
     * @param name     String
     * @param code     String
     * @param filterId Long
     * @return int 0 - not exist
     */
    @SelectProvider(type = OrganizationSqlProvider.class, method = "getSameNameOrCodeRole")
    int getSameNameOrCodeRole(@Param("name") String name, @Param("code") String code, @Param("filterId") Long filterId);

    /**
     * @param model       OrganizationDO
     * @param orderFields List
     * @return List
     */
    @SelectProvider(type = OrganizationSqlProvider.class, method = "listWithOrder")
    List<OrganizationDO> listWithOrder(@Param("model") OrganizationDO model,
                                       @Param("orderFields") List<String> orderFields);
}

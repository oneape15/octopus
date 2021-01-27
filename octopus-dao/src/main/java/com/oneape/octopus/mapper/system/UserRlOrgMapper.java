package com.oneape.octopus.mapper.system;

import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.domain.system.UserRlOrgDO;
import com.oneape.octopus.dto.system.OrgUserSizeDTO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.system.provider.UserRlOrgSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 15:25.
 * Modify:
 */
public interface UserRlOrgMapper {
    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = UserRlOrgSqlProvider.class, method = "insert")
    int insert(UserRlOrgDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserRlOrgSqlProvider.class, method = "updateById")
    int update(UserRlOrgDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserRlOrgSqlProvider.class, method = "deleteById")
    int deleteById(UserRlOrgDO model);

    /**
     * Delete data by Model (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserRlOrgSqlProvider.class, method = "delete")
    int delete(UserRlOrgDO model);

    /**
     * Delete the relationship between user and org base on userId.
     *
     * @param userId Long
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserRlOrgSqlProvider.class, method = "deleteByUserId")
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * Delete the relationship between user and org base on orgId.
     *
     * @param orgId Long
     * @return int 1 - success; 0 - fail.
     */
    int deleteByOrgId(@Param("orgId") Long orgId);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = UserRlOrgSqlProvider.class, method = "findById")
    UserRlOrgDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = UserRlOrgSqlProvider.class, method = "list")
    List<UserRlOrgDO> list(@Param("model") UserRlOrgDO model);

    /**
     * Get usage of the org.
     *
     * @param orgId Long
     * @return int
     */
    @Select({
            "SELECT COUNT(0) FROM " + UserRlOrgSqlProvider.TABLE_NAME +
                    " WHERE " + BaseSqlProvider.FIELD_ARCHIVE + " = 0 AND org_id = #{orgId}"
    })
    int getUseSize(@Param("orgId") Long orgId);

    /**
     * Gets the number of nodes associated under the org
     *
     * @param orgIds List
     * @return Map
     */
    @SelectProvider(type = UserRlOrgSqlProvider.class, method = "getOrgLinkUserSize")
    List<OrgUserSizeDTO> getOrgLinkUserSize(@Param("orgIds") List<Long> orgIds);

    /**
     * Query the list of users based on org id.
     *
     * @param orgId Long
     * @return List
     */
    @SelectProvider(type = UserRlOrgSqlProvider.class, method = "getUserByOrgId")
    List<UserDO> getUserByOrgId(@Param("orgId") Long orgId);
}

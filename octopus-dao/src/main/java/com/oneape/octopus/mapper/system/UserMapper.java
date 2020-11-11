package com.oneape.octopus.mapper.system;

import com.oneape.octopus.domain.system.UserDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.mapper.system.provider.UserSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = UserSqlProvider.class, method = "insert")
    int insert(UserDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "updateById")
    int update(UserDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "deleteById")
    int delete(UserDO model);

    @UpdateProvider(type = UserSqlProvider.class, method = "delByIds")
    int delByIds(@Param("userIds") List<Long> userIds, @Param("modifier") Long modifier);

    /**
     * Same name detection
     */
    @SelectProvider(type = UserSqlProvider.class, method = "sameNameCheck")
    int sameNameCheck(@Param("username") String username, @Param("filterId") Long filterId);

    /**
     * Find by primary key.
     *
     * @param id Long
     * @return T
     */
    @SelectProvider(type = UserSqlProvider.class, method = "findById")
    UserDO findById(@Param("id") Long id);

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return List
     */
    @SelectProvider(type = UserSqlProvider.class, method = "list")
    List<UserDO> list(@Param("model") UserDO model);

    /**
     * Query user information by login name.
     *
     * @param username String
     * @return UserDO
     */
    @Select({
            "SELECT * FROM " + UserSqlProvider.TABLE_NAME
                    + " WHERE  " + BaseSqlProvider.FIELD_ARCHIVE + " = 0 AND username= #{username}"
    })
    UserDO getByUsername(@Param("username") String username);
}

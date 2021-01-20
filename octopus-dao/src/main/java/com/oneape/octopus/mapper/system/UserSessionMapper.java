package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.UserSessionSqlProvider;
import com.oneape.octopus.domain.system.UserSessionDO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserSessionMapper {

    /**
     * Add data to table.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @InsertProvider(type = UserSessionSqlProvider.class, method = "insert")
    int insert(UserSessionDO model);

    /**
     * Update data by primary key.
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserSessionSqlProvider.class, method = "updateById")
    int update(UserSessionDO model);

    /**
     * Delete data by primary key (soft delete, update archive state).
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @UpdateProvider(type = UserSessionSqlProvider.class, method = "deleteById")
    int delete(UserSessionDO model);

    @Select({
            "SELECT * FROM " + UserSessionSqlProvider.TABLE_NAME + " WHERE token = #{token}"
    })
    UserSessionDO findByToken(@Param("token") String token);

    @Select({
            "SELECT * FROM " + UserSessionSqlProvider.TABLE_NAME + " WHERE user_id = #{userId} AND app_type = #{appType}"
    })
    UserSessionDO findByUserId(@Param("userId") Long userId, @Param("appType") Integer appType);

    /**
     * set the user token to expire.
     *
     * @param userId Long
     * @return int
     */
    @UpdateProvider(type = UserSessionSqlProvider.class, method = "setToken2expire")
    int setToken2expire(@Param("userId") Long userId);


}

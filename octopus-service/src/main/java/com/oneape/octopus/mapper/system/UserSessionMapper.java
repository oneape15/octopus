package com.oneape.octopus.mapper.system;

import com.oneape.octopus.mapper.system.provider.UserSessionSqlProvider;
import com.oneape.octopus.model.DO.system.UserSessionDO;
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
}

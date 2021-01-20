package com.oneape.octopus.mapper.system.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.domain.system.UserSessionDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class UserSessionSqlProvider extends BaseSqlProvider<UserSessionDO> {

    public static final String TABLE_NAME = "sys_user_session";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String setToken2expire(@Param("userId") Long userId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET("expire_at = " + System.currentTimeMillis())
                .WHERE("user_id = #{userId}")
                .toString();
    }
}

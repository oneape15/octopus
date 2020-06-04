package com.oneape.octopus.mapper.system.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.UserSessionDO;

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
}

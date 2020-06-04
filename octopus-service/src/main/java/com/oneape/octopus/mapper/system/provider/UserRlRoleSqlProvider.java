package com.oneape.octopus.mapper.system.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.UserRlRoleDO;

public class UserRlRoleSqlProvider extends BaseSqlProvider<UserRlRoleDO> {
    public static final String TABLE_NAME = "sys_user_rl_role";

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

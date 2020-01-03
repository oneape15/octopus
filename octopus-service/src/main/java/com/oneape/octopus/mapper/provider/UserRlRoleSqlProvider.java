package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.UserRlRoleDO;

public class UserRlRoleSqlProvider extends BaseSqlProvider<UserRlRoleDO> {
    public static final String TABLE_NAME = "sys_user_rl_role";

    /**
     * 获取表名
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}

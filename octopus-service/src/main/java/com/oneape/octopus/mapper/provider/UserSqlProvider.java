package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.UserDO;

public class UserSqlProvider extends BaseSqlProvider<UserDO> {

    public static final String TABLE_NAME = "sys_user";

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

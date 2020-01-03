package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.RoleRlResourceDO;

public class RoleRlResourceSqlProvider extends BaseSqlProvider<RoleRlResourceDO> {
    public static final String TABLE_NAME = "sys_role_rl_resource";

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

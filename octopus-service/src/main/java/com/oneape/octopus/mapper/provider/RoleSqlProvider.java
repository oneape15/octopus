package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.RoleDO;

public class RoleSqlProvider extends BaseSqlProvider<RoleDO> {
    public static final String TABLE_NAME = "sys_role";

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

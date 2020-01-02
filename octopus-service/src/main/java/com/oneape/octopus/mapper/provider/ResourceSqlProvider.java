package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.ResourceDO;

public class ResourceSqlProvider extends BaseSqlProvider<ResourceDO> {
    public static final String TABLE_NAME = "sys_resource";

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

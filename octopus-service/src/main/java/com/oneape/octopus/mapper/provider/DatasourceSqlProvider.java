package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.DatasourceDO;

public class DatasourceSqlProvider extends BaseSqlProvider<DatasourceDO> {
    public static final String TABLE_NAME = "datasource";

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

package com.oneape.octopus.mapper.system.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.CommonInfoDO;

public class CommonInfoSqlProvider extends BaseSqlProvider<CommonInfoDO> {
    public static final String TABLE_NAME = "sys_common_info";

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

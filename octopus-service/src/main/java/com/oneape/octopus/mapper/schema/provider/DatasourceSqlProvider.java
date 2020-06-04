package com.oneape.octopus.mapper.schema.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.schema.DatasourceDO;

public class DatasourceSqlProvider extends BaseSqlProvider<DatasourceDO> {
    public static final String TABLE_NAME = "datasource";

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

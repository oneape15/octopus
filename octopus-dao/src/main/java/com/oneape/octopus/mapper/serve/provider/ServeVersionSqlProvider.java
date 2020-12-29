package com.oneape.octopus.mapper.serve.provider;

import com.oneape.octopus.domain.serve.ServeVersionDO;
import com.oneape.octopus.mapper.BaseSqlProvider;

public class ServeVersionSqlProvider extends BaseSqlProvider<ServeVersionDO> {
    public static final String TABLE_NAME = "serve_version";

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

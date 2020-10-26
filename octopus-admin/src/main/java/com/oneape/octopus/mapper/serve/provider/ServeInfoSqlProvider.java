package com.oneape.octopus.mapper.serve.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.domain.serve.ServeInfoDO;

public class ServeInfoSqlProvider extends BaseSqlProvider<ServeInfoDO> {
    public static final String TABLE_NAME = "serve_info";

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

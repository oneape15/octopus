package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.domain.peekdata.ModelTagDO;

public class ModelTagSqlProvider extends BaseSqlProvider<ModelTagDO> {
    public static final String TABLE_NAME = "pd_model_tag";

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

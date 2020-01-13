package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ModelTagDO;

public class ModelTagSqlProvider extends BaseSqlProvider<ModelTagDO> {
    public static final String TABLE_NAME = "pd_model_tag";

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

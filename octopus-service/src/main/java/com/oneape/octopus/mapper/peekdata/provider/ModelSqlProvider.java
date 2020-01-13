package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.peekdata.ModelDO;

public class ModelSqlProvider extends BaseSqlProvider<ModelDO> {
    public static final String TABLE_NAME = "pd_model";

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
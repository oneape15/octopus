package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.peekdata.PeekFieldDO;

public class PeekFieldSqlProvider extends BaseSqlProvider<PeekFieldDO> {
    public static final String TABLE_NAME = "pd_peek_field";

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

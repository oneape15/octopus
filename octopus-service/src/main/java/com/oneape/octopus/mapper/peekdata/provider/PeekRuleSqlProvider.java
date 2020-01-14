package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.peekdata.PeekRuleDO;

public class PeekRuleSqlProvider extends BaseSqlProvider<PeekRuleDO> {
    public static final String TABLE_NAME = "pd_peek_rule";

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

package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.domain.peekdata.PeekRuleDO;

public class PeekRuleSqlProvider extends BaseSqlProvider<PeekRuleDO> {
    public static final String TABLE_NAME = "pd_peek_rule";

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

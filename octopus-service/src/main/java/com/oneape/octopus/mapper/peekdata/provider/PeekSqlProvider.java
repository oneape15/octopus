package com.oneape.octopus.mapper.peekdata.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.peekdata.PeekDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class PeekSqlProvider extends BaseSqlProvider<PeekDO> {
    public static final String TABLE_NAME = "pd_peek";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * 取数次数加1
     *
     * @param peekId Long
     * @return String
     */
    public String incPeekTime(@Param("peekId") Long peekId) {
        return new SQL() {
            {
                UPDATE(getTableName());
                SET("peek_time = peek_time + 1");
            }
        }.toString();
    }
}

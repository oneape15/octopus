package com.oneape.octopus.mapper.report.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportDO;

public class ReportSqlProvider extends BaseSqlProvider<ReportDO> {
    public static final String TABLE_NAME = "report";

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

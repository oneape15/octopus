package com.oneape.octopus.mapper.report.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportSqlLogDO;

public class ReportSqlLogSqlProvider extends BaseSqlProvider<ReportSqlLogDO> {
    public static final String TABLE_NAME = "r_report_sql_log";

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

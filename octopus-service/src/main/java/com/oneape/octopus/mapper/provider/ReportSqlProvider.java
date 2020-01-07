package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.ReportSqlDO;

public class ReportSqlProvider extends BaseSqlProvider<ReportSqlDO> {
    public static final String TABLE_NAME = "r_report_sql";

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
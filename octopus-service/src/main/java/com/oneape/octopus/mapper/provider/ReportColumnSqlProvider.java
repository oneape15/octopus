package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.ReportColumnDO;

public class ReportColumnSqlProvider extends BaseSqlProvider<ReportColumnDO> {
    public static final String TABLE_NAME = "r_report_column";

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

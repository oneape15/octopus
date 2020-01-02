package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.ReportDO;

public class ReportSqlProvider extends BaseSqlProvider<ReportDO> {
    public static final String TABLE_NAME = "report";

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

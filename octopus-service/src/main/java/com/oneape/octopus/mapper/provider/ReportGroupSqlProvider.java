package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.ReportGroupDO;

public class ReportGroupSqlProvider extends BaseSqlProvider<ReportGroupDO> {
    public static final String TABLE_NAME = "r_report_group";

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

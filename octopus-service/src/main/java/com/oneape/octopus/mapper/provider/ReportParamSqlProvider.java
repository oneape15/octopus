package com.oneape.octopus.mapper.provider;

import com.oneape.octopus.model.DO.ReportParamDO;

public class ReportParamSqlProvider extends BaseSqlProvider<ReportParamDO> {
    public static final String TABLE_NAME = "r_report_param";

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

package com.oneape.octopus.mapper.report.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportParamDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

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

    public String deleteByReportId(@Param("reportId") Long reportId) {
        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = unix_timestamp(now())");
                WHERE(" report_id = #{reportId}");
            }
        }.toString();
    }
}

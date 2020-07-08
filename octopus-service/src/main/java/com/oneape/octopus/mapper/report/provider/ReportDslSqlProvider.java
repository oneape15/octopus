package com.oneape.octopus.mapper.report.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportDslDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class ReportDslSqlProvider extends BaseSqlProvider<ReportDslDO> {
    public static final String TABLE_NAME = "report_dsl";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


    public String findByReportId(@Param("reportId") Long reportId) {
        return new SQL().SELECT("*")
                .FROM(getTableName())
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        " report_id = #{reportId}")
                .LIMIT(1)
                .toString();
    }

    public String deleteByReportId(@Param("reportId") Long reportId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + BaseSqlProvider.DB_CURRENT_TIME)
                .WHERE(" report_id = #{reportId}")
                .toString();
    }
}

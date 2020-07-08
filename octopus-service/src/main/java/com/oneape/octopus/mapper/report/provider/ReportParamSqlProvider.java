package com.oneape.octopus.mapper.report.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportParamDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class ReportParamSqlProvider extends BaseSqlProvider<ReportParamDO> {
    public static final String TABLE_NAME = "report_param";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String deleteByReportId(@Param("reportId") Long reportId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + BaseSqlProvider.DB_CURRENT_TIME)
                .WHERE(" report_id = #{reportId}")
                .toString();
    }

    public String deleteByNames(@Param("reportId") Long reportId, @Param("names") List<String> names) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + BaseSqlProvider.DB_CURRENT_TIME)
                .WHERE("report_id = #{reportId}",
                        "name IN ('" + Joiner.on("','").join(names) + "')")
                .toString();
    }

    public String findByReportId(@Param("reportId") Long reportId) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(), "report_id = #{reportId}")
                .ORDER_BY("sort_id ASC")
                .toString();
    }
}

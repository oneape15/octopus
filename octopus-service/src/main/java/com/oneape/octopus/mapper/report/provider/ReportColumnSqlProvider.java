package com.oneape.octopus.mapper.report.provider;

import com.oneape.octopus.model.enums.Archive;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.ReportColumnDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class ReportColumnSqlProvider extends BaseSqlProvider<ReportColumnDO> {
    public static final String TABLE_NAME = "report_column";

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
        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + BaseSqlProvider.DB_CURRENT_TIME);
                WHERE(" report_id = #{reportId}");
            }
        }.toString();
    }

    public String deleteByNames(@Param("reportId") Long reportId, @Param("names") List<String> names) {
        StringBuilder sb = new StringBuilder(" name IN (");
        int i = 0;
        for (String name : names) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("'").append(name).append("'");
            i++;
        }
        sb.append(")");

        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + BaseSqlProvider.DB_CURRENT_TIME);
                WHERE(" report_id = #{reportId}", sb.toString());
            }
        }.toString();
    }
}

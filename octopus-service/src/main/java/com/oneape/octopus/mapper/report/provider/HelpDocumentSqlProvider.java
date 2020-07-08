package com.oneape.octopus.mapper.report.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.report.HelpDocumentDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class HelpDocumentSqlProvider extends BaseSqlProvider<HelpDocumentDO> {
    public static final String TABLE_NAME = "help_document";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


    public String findByBizInfo(@Param("bizType") Integer bizType, @Param("bizId") Long bizId) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        " biz_type = #{bizType}",
                        " biz_id = #{bizId}")
                .LIMIT(1)
                .toString();
    }

    public String deleteByBizInfo(@Param("bizType") Integer bizType, @Param("bizId") Long bizId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        FIELD_MODIFIED + " = " + DB_CURRENT_TIME)
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        " biz_type = #{bizType}",
                        " biz_id = #{bizId}")
                .toString();
    }
}

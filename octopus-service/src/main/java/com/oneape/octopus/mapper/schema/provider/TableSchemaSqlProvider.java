package com.oneape.octopus.mapper.schema.provider;

import com.google.common.base.Preconditions;
import com.oneape.octopus.model.enums.Archive;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.schema.TableColumnDO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class TableSchemaSqlProvider extends BaseSqlProvider<TableColumnDO> {
    public static final String TABLE_NAME = "table_schema";

    /**
     * get table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String dropTableBy(@Param("dsId") Long dsId, @Param("tableNames") List<String> tableNames) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(tableNames), "The data table name cannot be empty.");

        StringBuilder sb = new StringBuilder(" table_name IN (");
        for (int i = 0; i < tableNames.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("'").append(tableNames.get(i)).append("'");
        }
        sb.append(" )");

        return new SQL() {
            {
                UPDATE(getTableName());
                SET("status = 1",
                        FIELD_MODIFIED + "=" + DB_CURRENT_TIME);
                WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "datasource_id = #{dsId}",
                        sb.toString());
            }
        }.toString();
    }
}

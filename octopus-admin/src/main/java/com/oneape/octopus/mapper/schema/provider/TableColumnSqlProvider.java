package com.oneape.octopus.mapper.schema.provider;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.domain.schema.TableColumnDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class TableColumnSqlProvider extends BaseSqlProvider<TableColumnDO> {
    public static final String TABLE_NAME = "table_column";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String dropColumnBy(@Param("dsId") Long dsId, @Param("tableName") String tableName, @Param("columns") List<String> columns) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(columns), "The data table  column name cannot be empty.");
        return new SQL()
                .UPDATE(getTableName())
                .SET("status = 1",
                        FIELD_MODIFIED + "=" + DB_CURRENT_TIME)
                .WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "datasource_id = #{dsId}",
                        "table_name = #{tableName}",
                        "name IN ('" + Joiner.on("','").join(columns) + "')")
                .toString();
    }

    public String updateTableColumnHeatValue(@Param("dsId") Long dsId, @Param("tableName") String tableName, @Param("columns") List<String> columns) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(columns), "The data table  column name cannot be empty.");
        return new SQL()
                .UPDATE(getTableName())
                .SET(" heat = heat + 1")
                .WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "datasource_id = #{dsId}",
                        "table_name = #{tableName}",
                        "name IN ('" + Joiner.on("','").join(columns) + "')")
                .toString();
    }
}

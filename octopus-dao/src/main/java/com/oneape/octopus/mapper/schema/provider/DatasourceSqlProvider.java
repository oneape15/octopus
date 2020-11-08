package com.oneape.octopus.mapper.schema.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.domain.schema.DatasourceDO;
import com.oneape.octopus.commons.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class DatasourceSqlProvider extends BaseSqlProvider<DatasourceDO> {
    public static final String TABLE_NAME = "datasource";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String getSameBy(@Param("name") String name, @Param("filterId") Long filterId) {
        List<String> wheres = new ArrayList<>();
        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        wheres.add("name = #{name}");
        if (filterId != null) {
            wheres.add("id != #{filterId}");
        }
        return new SQL()
                .SELECT("COUNT(0)")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .toString();
    }
}

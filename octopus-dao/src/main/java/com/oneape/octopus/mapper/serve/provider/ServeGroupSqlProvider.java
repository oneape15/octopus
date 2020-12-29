package com.oneape.octopus.mapper.serve.provider;

import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.domain.serve.ServeGroupDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 14:50.
 * Modify:
 */
public class ServeGroupSqlProvider extends BaseSqlProvider<ServeGroupDO> {
    public static final String TABLE_NAME = "serve_group";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * Check the name have exist.
     *
     * @param name     String
     * @param filterId Long
     */
    public String checkHasTheSameName(@Param("name") String name, @Param("filterId") Long filterId) {
        List<String> wheres = new ArrayList<>();
        wheres.add(BaseSqlProvider.FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        wheres.add("name = #{name}");
        if (filterId != null) {
            wheres.add("id != #{filterId}");
        }

        return new SQL()
                .SELECT("COUNT(0)")
                .FROM(TABLE_NAME)
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .toString();
    }

}

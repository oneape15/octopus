package com.oneape.octopus.mapper.wharehouse.provider;

import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.domain.warehouse.DdlAuditInfoDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-02-25 16:08.
 * Modify:
 */
public class DdlAuditInfoSqlProvider extends BaseSqlProvider<DdlAuditInfoDO> {
    public static final String TABLE_NAME = "ddl_audit_info";

    /**
     * get table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String checkSameName(@Param("name") String name, @Param("filterId") Long filterId) {
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

package com.oneape.octopus.mapper.system.provider;

import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.domain.system.OrganizationDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 14:38.
 * Modify:
 */
public class OrganizationSqlProvider extends BaseSqlProvider<OrganizationDO> {

    public static final String TABLE_NAME = "sys_org";

    /**
     * get table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String getSameNameOrCodeRole(@Param("name") String name, @Param("code") String code, @Param("filterId") Long filterId) {
        StringBuilder sb = new StringBuilder();
        sb.append(FIELD_ARCHIVE).append("=").append(Archive.NORMAL.value())
                .append(" AND ( name = #{name} OR code = #{code} )");
        if (filterId != null) {
            sb.append(" AND id !=#{filterId}");
        }
        return new SQL()
                .SELECT("count(0)")
                .FROM(getTableName())
                .WHERE(sb.toString())
                .toString();
    }
}

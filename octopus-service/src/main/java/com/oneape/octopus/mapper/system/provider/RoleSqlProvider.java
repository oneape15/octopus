package com.oneape.octopus.mapper.system.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.RoleDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class RoleSqlProvider extends BaseSqlProvider<RoleDO> {
    public static final String TABLE_NAME = "sys_role";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String findRoleByUserId(@Param("userId") Long userId) {
        // Get the role id by user id.
        String subSql = new SQL()
                .SELECT_DISTINCT("role_id")
                .FROM(UserRlRoleSqlProvider.TABLE_NAME)
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "user_id = #{userId}")
                .toString();

        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "id IN (" + subSql + ")")
                .toString();
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

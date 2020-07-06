package com.oneape.octopus.mapper.system.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.ResourceDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class ResourceSqlProvider extends BaseSqlProvider<ResourceDO> {
    public static final String TABLE_NAME = "sys_resource";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String listByRoleIds(@Param("roleIds") List<Long> roleIds) {
        return new SQL() {
            {
                SELECT("res.*", "rl.mask AS mask");
                FROM(getTableName() + " AS res");
                INNER_JOIN(RoleRlResourceSqlProvider.TABLE_NAME + " AS rl ON rl.resource_id = res.id");
                WHERE("res." + FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "rl." + FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "rl.role_id IN (" + Joiner.on(",").join(roleIds) + ")");
                ORDER_BY("level ASC", "sortId ASC");
            }
        }.toString();
    }


    public String getSameBy(@Param("parentId") Long parentId, @Param("name") String name, @Param("filterId") Long filterId) {
        List<String> wheres = new ArrayList<>();
        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        wheres.add("parent_id=#{parentId}");
        wheres.add("name = #{name}");
        if (filterId != null) {
            wheres.add("id != #{filterId}");
        }
        return new SQL() {
            {
                SELECT("COUNT(0)");
                FROM(getTableName());
                WHERE(wheres.toArray(new String[wheres.size()]));
            }
        }.toString();
    }
}

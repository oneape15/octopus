package com.oneape.octopus.mapper.system.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.RoleRlResourceDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class RoleRlResourceSqlProvider extends BaseSqlProvider<RoleRlResourceDO> {
    public static final String TABLE_NAME = "sys_role_rl_resource";

    /**
     * 获取表名
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String getResIdByRoleIds(@Param("roleIds") List<Long> roleIds) {
        return new SQL() {
            {
                SELECT("resource_id", "role_id", "mask");
                FROM(getTableName());
                WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "role_id IN (" + Joiner.on(",").join(roleIds) + ")");
            }
        }.toString();
    }
}

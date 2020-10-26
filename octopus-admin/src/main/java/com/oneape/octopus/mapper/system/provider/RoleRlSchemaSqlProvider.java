package com.oneape.octopus.mapper.system.provider;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.domain.system.RoleRlSchemaDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class RoleRlSchemaSqlProvider extends BaseSqlProvider<RoleRlSchemaDO> {
    public static final String TABLE_NAME = "sys_role_rl_schema";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String deleteByRoleId(@Param("roleId") Long roleId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + "=" + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + "=" + DB_CURRENT_TIME)
                .WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "role_id=#{roleId}")
                .toString();
    }

    public String findByRoleId(@Param("roleId") Long roleId) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "role_id=#{roleId}")
                .toString();
    }

    public String deleteByIds(@Param("ids") List<Long> ids) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(ids), "The id list is empty.");
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + "=" + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + "=" + DB_CURRENT_TIME)
                .WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "id IN (" + Joiner.on(",").join(ids) + ")")
                .toString();
    }
}

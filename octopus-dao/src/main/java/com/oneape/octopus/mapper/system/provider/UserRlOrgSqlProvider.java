package com.oneape.octopus.mapper.system.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.domain.system.UserRlOrgDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-25 15:24.
 * Modify:
 */
public class UserRlOrgSqlProvider extends BaseSqlProvider<UserRlOrgDO> {
    public static final String TABLE_NAME = "sys_user_rl_org";

    /**
     * get table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String deleteByUserId(@Param("userId") Long userId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + "=" + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + "=" + DB_CURRENT_TIME)
                .WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "user_id=#{userId}")
                .toString();
    }

    public String deleteByOrgId(@Param("orgId") Long orgId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + "=" + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + "=" + DB_CURRENT_TIME)
                .WHERE(FIELD_ARCHIVE + "=" + Archive.NORMAL.value(),
                        "org_id=#{orgId}")
                .toString();
    }

    public String getOrgLinkUserSize(@Param("orgIds") List<Long> orgIds) {
        return new SQL()
                .SELECT("org_id", "COUNT(0) as size")
                .FROM(TABLE_NAME)
                .WHERE(BaseSqlProvider.FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "org_id IN (" + Joiner.on(",").join(orgIds) + ")")
                .GROUP_BY("org_id")
                .toString();
    }
}

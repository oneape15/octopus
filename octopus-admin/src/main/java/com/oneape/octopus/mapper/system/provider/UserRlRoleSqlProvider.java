package com.oneape.octopus.mapper.system.provider;

import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.domain.system.UserRlRoleDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class UserRlRoleSqlProvider extends BaseSqlProvider<UserRlRoleDO> {
    public static final String TABLE_NAME = "sys_user_rl_role";

    /**
     * Gets the table name.
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
}

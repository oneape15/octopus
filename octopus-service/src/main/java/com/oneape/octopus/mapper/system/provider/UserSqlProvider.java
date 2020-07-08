package com.oneape.octopus.mapper.system.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.model.enums.Archive;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.UserDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class UserSqlProvider extends BaseSqlProvider<UserDO> {

    public static final String TABLE_NAME = "sys_user";

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
     * Delete the user based on the user Id.
     *
     * @param userIds  List
     * @param modifier Long
     * @return String
     */
    public String delByIds(@Param("userIds") List<Long> userIds, @Param("modifier") Long modifier) {
        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIER + " = #{modifier}",
                        FIELD_MODIFIED + " = unix_timestamp(now())")
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "id IN (" + Joiner.on(",").join(userIds) + ")").toString();
    }

    public String sameNameCheck(@Param("username") String username, @Param("filterId") Long filterId) {
        List<String> wheres = new ArrayList<>();
        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        wheres.add("username = #{username}");
        if (filterId != null) {
            wheres.add("id != #{filterId}");
        }
        return new SQL()
                .SELECT("count(0)")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()])).toString();
    }
}

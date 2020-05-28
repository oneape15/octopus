package com.oneape.octopus.mapper.system.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.common.enums.Archive;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.DO.system.UserDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

public class UserSqlProvider extends BaseSqlProvider<UserDO> {

    public static final String TABLE_NAME = "sys_user";

    /**
     * 获取表名
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }


    /**
     * 根据用户Id删除用户
     *
     * @param userIds  List
     * @param modifier Long 修改人id
     * @return String
     */
    public String delByIds(@Param("userIds") List<Long> userIds, @Param("modifier") Long modifier) {
        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIER + " = #{modifier}",
                        FIELD_MODIFIED + " = unix_timestamp(now())");
                WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "id IN (" + Joiner.on(",").join(userIds) + ")");
            }
        }.toString();
    }
}

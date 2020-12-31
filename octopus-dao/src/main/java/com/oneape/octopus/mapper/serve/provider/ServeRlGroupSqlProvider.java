package com.oneape.octopus.mapper.serve.provider;

import com.google.common.base.Joiner;
import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.domain.serve.ServeRlGroupDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-16 14:50.
 * Modify:
 */
public class ServeRlGroupSqlProvider extends BaseSqlProvider<ServeRlGroupDO> {
    public static final String TABLE_NAME = "serve_rl_group";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String getGroupLinkServeSize(@Param("groupIds") List<Long> groupIds) {
        return new SQL()
                .SELECT("group_id", "COUNT(0) as size")
                .FROM(TABLE_NAME)
                .WHERE(BaseSqlProvider.FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "group_id IN (" + Joiner.on(",").join(groupIds) + ")")
                .GROUP_BY("group_id")
                .toString();
    }


}

package com.oneape.octopus.mapper.serve.provider;

import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.commons.enums.VersionType;
import com.oneape.octopus.domain.serve.ServeVersionDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class ServeVersionSqlProvider extends BaseSqlProvider<ServeVersionDO> {
    public static final String TABLE_NAME = "serve_version";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String changePublish2History(@Param("serveId") Long serveId) {
        return new SQL()
                .UPDATE(getTableName())
                .SET("versionType = " + VersionType.HISTORY)
                .WHERE("serve_id = #{serveId}", BaseSqlProvider.FIELD_ARCHIVE + " = " + Archive.NORMAL)
                .toString();
    }

}

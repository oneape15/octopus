package com.oneape.octopus.mapper.serve.provider;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.dto.EntityAttribute;
import com.oneape.octopus.commons.enums.Archive;
import com.oneape.octopus.commons.enums.ServeStatusType;
import com.oneape.octopus.commons.value.EntityColumnUtils;
import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.mapper.BaseSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public class ServeInfoSqlProvider extends BaseSqlProvider<ServeInfoDO> {
    public static final String TABLE_NAME = "serve_info";

    /**
     * Gets the table name.
     *
     * @return String
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    public String listWithOutTextField(@Param("model") ServeInfoDO model) {
        Preconditions.checkNotNull(model, "The Serve Object is empty.");
        List<EntityAttribute> fields = EntityColumnUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getColumnName();
                wheres.add("`" + columnName + "` = #{model." + field.getName() + "}");
            }
        });

        return new SQL()
                .SELECT("id", "name", "icon", "time_based", "serve_type", "visual_type", "sort_id",
                        "comment", "archive", "created", "creator", "modified", "modifier")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[0]))
                .toString();
    }

    public String hasSameName(@Param("name") String name, @Param("filterId") Long filterId) {
        List<String> wheres = new ArrayList<>();
        wheres.add("name = #{name}");
        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        if (filterId != null) {
            wheres.add("id != #{filterId}");
        }

        return new SQL()
                .SELECT("COUNT(0)")
                .FROM(TABLE_NAME)
                .WHERE(wheres.toArray(new String[0]))
                .toString();
    }

    public String countArchiveServe(@Param("serveType") String serveType) {
        return new SQL()
                .SELECT("COUNT(0)")
                .FROM(TABLE_NAME)
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "serve_type = #{serveType}",
                        "status = '" + ServeStatusType.ARCHIVE.name() + "'")
                .toString();
    }

    public String countPersonalServe(@Param("serveType") String serveType, @Param("userId") Long userId) {
        String link2GroupId = new SQL()
                .SELECT("DISTINCT serve_id")
                .FROM(TABLE_NAME + " AS t")
                .LEFT_OUTER_JOIN(ServeRlGroupSqlProvider.TABLE_NAME + " AS rl ON t.id = rl.serve_id")
                .WHERE("t." + FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "t.serve_type = #{serveType}",
                        "t.status = '" + ServeStatusType.EDIT.name() + "'",
                        "t." + FIELD_CREATOR + " = #{userId}",
                        "rl." + FIELD_ARCHIVE + " = " + Archive.NORMAL.value()
                )
                .toString();
        return new SQL()
                .SELECT("COUNT(DISTINCT id)")
                .FROM(TABLE_NAME)
                .WHERE(FIELD_ARCHIVE + " = " + Archive.NORMAL.value(),
                        "serve_type = #{serveType}",
                        "status = '" + ServeStatusType.EDIT.name() + "'",
                        FIELD_CREATOR + " = #{userId}",
                        "id NOT IN (" + link2GroupId + ")")
                .toString();
    }

}

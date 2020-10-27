package com.oneape.octopus.mapper.serve.provider;

import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.dto.BeanProperties;
import com.oneape.octopus.commons.value.BeanUtils;
import com.oneape.octopus.mapper.BaseSqlProvider;
import com.oneape.octopus.model.domain.serve.ServeInfoDO;
import com.oneape.octopus.model.enums.Archive;
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
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                wheres.add("`" + columnName + "` = #{model." + field.getName() + "}");
            }
        });

        return new SQL()
                .SELECT("id", "name", "icon", "time_based", "serve_type", "visual_type", "sort_id",
                        "comment", "archive", "created", "creator", "modified", "modifier")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()]))
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
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .toString();
    }

}

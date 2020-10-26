package com.oneape.octopus.mapper;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.SessionThreadLocal;
import com.oneape.octopus.commons.dto.BeanProperties;
import com.oneape.octopus.commons.value.BeanUtils;
import com.oneape.octopus.model.domain.BaseDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSqlProvider<T extends BaseDO> {
    /**
     * The name of primary key.
     */
    public final static String FIELD_PRIMARY_ID = "id";
    /**
     * The field of create time.
     */
    public final static String FIELD_CREATED    = "created";
    /**
     * The field of creator.
     */
    public final static String FIELD_CREATOR    = "creator";
    /**
     * The field of update time.
     */
    public final static String FIELD_MODIFIED   = "modified";
    /**
     * the field of modifier.
     */
    public final static String FIELD_MODIFIER   = "modifier";
    /**
     * The field of soft deleted.
     */
    public final static String FIELD_ARCHIVE    = "archive";

    /**
     * The database gets the current timestamp
     */
    public final static String DB_CURRENT_TIME = "unix_timestamp(now()) * 1000";

    /**
     * get table name.
     *
     * @return String
     */
    public abstract String getTableName();

    /**
     * insert data to db.
     *
     * @param model T
     * @return String
     */
    public String insert(T model) {
        Preconditions.checkNotNull(model, "The insert data set entity is empty.");

        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (BeanProperties field : fields) {
            // Fields for special processing.
            if (StringUtils.equals(field.getName(), FIELD_PRIMARY_ID)) {
                columns.add(FIELD_PRIMARY_ID);
                values.add("#{" + FIELD_PRIMARY_ID + "}");
                continue;
            }
            if (StringUtils.equals(field.getName(), FIELD_CREATED)) {
                columns.add(FIELD_CREATED);
                values.add(DB_CURRENT_TIME);
                continue;
            }
            if (StringUtils.equals(field.getName(), FIELD_CREATOR)) {
                columns.add(FIELD_CREATOR);
                values.add(SessionThreadLocal.getUserIdOfDefault(-1L) + "");
                continue;
            }

            if (field.getValue() == null) {
                continue;
            }
            columns.add("`" + field.getDbColumnName() + "`");
            values.add("#{" + field.getName() + "}");
        }

        Preconditions.checkArgument(fields.size() != 0, "The insert data action field is empty.");
        return new SQL()
                .INSERT_INTO(getTableName())
                .INTO_COLUMNS(columns.toArray(new String[columns.size()]))
                .INTO_VALUES(values.toArray(new String[values.size()]))
                .toString();
    }

    /**
     * Update the non-NULL properties in the model based on the Id.
     *
     * @param model T
     * @return String
     */
    public String updateById(T model) {
        Preconditions.checkNotNull(model, "The update data set entity is empty.");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        Preconditions.checkArgument(primaryKeyHasValue(fields), "The primary key is empty.");

        List<String> sets = new ArrayList<>();

        fields.forEach(field -> {

            String columnName = field.getDbColumnName();
            if (StringUtils.equals(columnName, FIELD_MODIFIED)) {
                sets.add(FIELD_MODIFIED + " = " + DB_CURRENT_TIME);
            } else if (StringUtils.equals(columnName, FIELD_MODIFIER)) {
                sets.add(FIELD_MODIFIER + " = " + SessionThreadLocal.getUserIdOfDefault(-1L) + "");
            } else {
                if (field.getValue() != null &&
                        !StringUtils.equalsAny(columnName, FIELD_PRIMARY_ID, FIELD_CREATED, FIELD_CREATOR)) {
                    if (StringUtils.isBlank(columnName)) {
                        columnName = field.getName();
                    }
                    sets.add("`" + columnName + "` = #{" + field.getName() + "}");
                }
            }
        });

        Preconditions.checkArgument(!sets.isEmpty(), "The update data action field is empty.");
        return new SQL()
                .UPDATE(getTableName())
                .SET(sets.toArray(new String[sets.size()]))
                .WHERE(FIELD_PRIMARY_ID + " =  #{" + FIELD_PRIMARY_ID + "}")
                .toString();
    }

    /**
     * Delete by Id.
     *
     * @param model T
     * @return String
     */
    public String deleteById(T model) {
        Preconditions.checkNotNull(model, "The data set entity is empty.");
        Preconditions.checkArgument(primaryKeyHasValue(model), "The primary key is empty.");

        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + DB_CURRENT_TIME,
                        FIELD_MODIFIER + " = #{" + FIELD_MODIFIER + "}")
                .WHERE(FIELD_PRIMARY_ID + "= #{" + FIELD_PRIMARY_ID + "}")
                .toString();
    }

    /**
     * Deletes according to incoming conditions.
     *
     * @param model T
     * @return String
     */
    public String delete(T model) {
        Preconditions.checkNotNull(model, "The delete data set entity is empty.");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> whereSql = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                if (StringUtils.isBlank(columnName)) {
                    columnName = field.getName();
                }
                whereSql.add("`" + columnName + "` = #{" + field.getName() + "}");
            }
        });

        return new SQL()
                .UPDATE(getTableName())
                .SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + DB_CURRENT_TIME,
                        FIELD_MODIFIER + " = #{" + FIELD_MODIFIER + "}")
                .WHERE(whereSql.toArray(new String[whereSql.size()]))
                .toString();
    }


    /**
     * Find by primary key.
     *
     * @param id Long
     * @return String
     */
    public String findById(@Param(FIELD_PRIMARY_ID) Long id) {
        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(FIELD_PRIMARY_ID + "= #{" + FIELD_PRIMARY_ID + "}")
                .toString();
    }

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return String
     */
    public String list(@Param("model") T model) {
        Preconditions.checkNotNull(model, "The query data set entity is empty.");
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
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .toString();
    }

    /**
     * Gets the number of data rows.
     *
     * @param model T
     * @return String
     */
    public String size(T model) {
        Preconditions.checkNotNull(model, "The query data set entity is empty.");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                wheres.add("`" + columnName + "` = #{" + field.getName() + "}");
            }
        });

        if (CollectionUtils.isEmpty(wheres)) {
            wheres.add(" 1 = 1 ");
        }

        return new SQL()
                .SELECT("COUNT(1)")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .toString();
    }

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model       T
     * @param orderFields List
     * @return String
     */
    public String listWithOrder(@Param("model") T model, @Param("orderFields") List<String> orderFields) {
        Preconditions.checkNotNull(model, "The query data set entity is empty.");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();
        List<String> orders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(orderFields)) {
            for (String tmp : orderFields) {
                if (StringUtils.isNotBlank(tmp)) {
                    orders.add(tmp);
                }
            }
        } else {
            orders.add(FIELD_CREATED + " DESC ");
        }

        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        fields.forEach(field -> {
            if (field.getValue() != null && !StringUtils.equals(FIELD_ARCHIVE, field.getName())) {
                String columnName = field.getDbColumnName();
                wheres.add("`" + columnName + "` = #{model." + field.getName() + "}");
            }
        });

        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(wheres.toArray(new String[wheres.size()]))
                .ORDER_BY(orders.toArray(new String[orders.size()]))
                .toString();
    }

    /**
     * OR is used to concatenate operations based on attribute values that are not null in the entity.
     *
     * @param model T
     * @return String
     */
    public String listOrLink(@Param("model") T model) {
        Preconditions.checkNotNull(model, "The query data set entity is empty.");
        List<BeanProperties> fields = BeanUtils.getFields(model);


        int index = 0;
        StringBuilder subWheres = new StringBuilder();
        for (BeanProperties field : fields) {
            if (field.getValue() == null) {
                continue;
            }
            String columnName = field.getDbColumnName();
            if (index > 0) {
                subWheres.append(" OR ");
            }
            subWheres.append("`").append(columnName).append("` = #{model.").append(field.getName()).append("}");
            index++;
        }

        StringBuilder wheres = new StringBuilder(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        if (index > 0) {
            wheres.append(" AND (").append(subWheres).append(")");
        }

        return new SQL()
                .SELECT("*")
                .FROM(getTableName())
                .WHERE(wheres.toString())
                .toString();
    }

    /**
     * Determines whether the primary key is empty.
     *
     * @param model T
     * @return boolean
     */
    private boolean primaryKeyHasValue(T model) {
        List<BeanProperties> fields = BeanUtils.getFields(model);
        return primaryKeyHasValue(fields);
    }

    /**
     * Determines whether the primary key is empty.
     *
     * @param fields List
     * @return boolean
     */
    private boolean primaryKeyHasValue(List<BeanProperties> fields) {
        if (CollectionUtils.isEmpty(fields)) {
            return false;
        }

        for (BeanProperties field : fields) {
            if (StringUtils.equals(field.getName(), FIELD_PRIMARY_ID) && field.getValue() != null) {
                return true;
            }
        }
        return false;
    }

}

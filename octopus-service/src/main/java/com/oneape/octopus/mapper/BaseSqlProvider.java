package com.oneape.octopus.mapper;

import com.google.common.base.Preconditions;
import com.oneape.octopus.common.SessionThreadLocal;
import com.oneape.octopus.common.enums.Archive;
import com.oneape.octopus.commons.dto.BeanProperties;
import com.oneape.octopus.commons.value.BeanUtils;
import com.oneape.octopus.model.DO.BaseDO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSqlProvider<T extends BaseDO> {
    /**
     * 主键字段名称
     */
    public final static String FIELD_PRIMARY_ID = "id";
    /**
     * 创建时间字段名称
     */
    public final static String FIELD_CREATED    = "created";
    /**
     * 创建人字段名称
     */
    public final static String FIELD_CREATOR    = "creator";
    /**
     * 修改时间字段名称
     */
    public final static String FIELD_MODIFIED   = "modified";
    /**
     * 修改人字段名称
     */
    public final static String FIELD_MODIFIER   = "modifier";
    /**
     * 归档字段名称
     */
    public final static String FIELD_ARCHIVE    = "archive";

    /***
     * 数据库获取当前时间戳
     */
    public final static String DB_CURRENT_TIME = "unix_timestamp(now()) * 1000";

    /**
     * get table name.
     *
     * @return String
     */
    public abstract String getTableName();

    /**
     * 插入操作
     *
     * @param model T
     * @return String
     */
    public String insert(T model) {
        Preconditions.checkNotNull(model, "插入操作实体不能为空");

        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (BeanProperties field : fields) {
            // 特殊处理的字段
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
            columns.add(field.getDbColumnName());
            values.add("#{" + field.getName() + "}");
        }

        Preconditions.checkArgument(fields.size() != 0, "插入数据操作字段为空");
        return new SQL() {
            {
                INSERT_INTO(getTableName());
                INTO_COLUMNS(columns.toArray(new String[columns.size()]));
                INTO_VALUES(values.toArray(new String[values.size()]));
            }
        }.toString();
    }

    /**
     * 根据Id更新model中不为null的属性
     *
     * @param model T
     * @return String
     */
    public String updateById(T model) {
        Preconditions.checkNotNull(model, "更新数据实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        Preconditions.checkArgument(primaryKeyHasValue(fields), "更新时，主键为空");

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
                    sets.add(columnName + " = #{" + field.getName() + "}");
                }
            }
        });

        Preconditions.checkArgument(!sets.isEmpty(), "更新数据操作字段为空");
        return new SQL() {
            {
                UPDATE(getTableName());
                SET(sets.toArray(new String[sets.size()]));
                WHERE(FIELD_PRIMARY_ID + " =  #{" + FIELD_PRIMARY_ID + "}");
            }
        }.toString();
    }

    /**
     * 根据Id删除
     *
     * @param model T
     * @return String
     */
    public String deleteById(T model) {
        Preconditions.checkNotNull(model, "删除操作实体为空");
        Preconditions.checkArgument(primaryKeyHasValue(model), "删除时，主键为空");

        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + DB_CURRENT_TIME,
                        FIELD_MODIFIER + " = #{" + FIELD_MODIFIER + "}");
                WHERE(FIELD_PRIMARY_ID + "= #{" + FIELD_PRIMARY_ID + "}");
            }
        }.toString();
    }

    /**
     * 根据传入条件进行删除
     *
     * @param model T
     * @return String
     */
    public String delete(T model) {
        Preconditions.checkNotNull(model, "删除操作实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> whereSql = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                if (StringUtils.isBlank(columnName)) {
                    columnName = field.getName();
                }
                whereSql.add(columnName + " = #{" + field.getName() + "}");
            }
        });

        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + " = " + Archive.ARCHIVE.value(),
                        FIELD_MODIFIED + " = " + DB_CURRENT_TIME,
                        FIELD_MODIFIER + " = #{" + FIELD_MODIFIER + "}");
                WHERE(whereSql.toArray(new String[whereSql.size()]));
            }
        }.toString();
    }


    /**
     * Find by primary key.
     *
     * @param id Long
     * @return String
     */
    public String findById(@Param(FIELD_PRIMARY_ID) Long id) {
        return new SQL() {
            {
                SELECT("*");
                FROM(getTableName());
                WHERE(FIELD_PRIMARY_ID + "= #{" + FIELD_PRIMARY_ID + "}");
            }
        }.toString();
    }

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model T
     * @return String
     */
    public String list(@Param("model") T model) {
        Preconditions.checkNotNull(model, "查询数据集实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        wheres.add(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                wheres.add(columnName + " = #{model." + field.getName() + "}");
            }
        });

        return new SQL() {
            {
                SELECT("*");
                FROM(getTableName());
                WHERE(wheres.toArray(new String[wheres.size()]));
            }
        }.toString();
    }

    /**
     * 获取数量SQL
     *
     * @param model T
     * @return String
     */
    public String size(T model) {
        Preconditions.checkNotNull(model, "查询数据集实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                wheres.add(columnName + " = #{" + field.getName() + "}");
            }
        });

        if (CollectionUtils.isEmpty(wheres)) {
            wheres.add(" 1 = 1 ");
        }

        return new SQL() {
            {
                SELECT("COUNT(1)");
                FROM(getTableName());
                WHERE(wheres.toArray(new String[wheres.size()]));
            }
        }.toString();
    }

    /**
     * The query is based on a property in the entity that is not null.
     *
     * @param model       T
     * @param orderFields List
     * @return String
     */
    public String listWithOrder(@Param("model") T model, @Param("orderFields") List<String> orderFields) {
        Preconditions.checkNotNull(model, "查询数据集实体为空");
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
                wheres.add(columnName + " = #{model." + field.getName() + "}");
            }
        });

        return new SQL() {
            {
                SELECT("*");
                FROM(getTableName());
                WHERE(wheres.toArray(new String[wheres.size()]));
                ORDER_BY(orders.toArray(new String[orders.size()]));
            }
        }.toString();
    }

    /**
     * 根据实体中不为null的属性值，使用 OR 连接起来进行操作
     *
     * @param model T
     * @return String
     */
    public String listOrLink(@Param("model") T model) {
        Preconditions.checkNotNull(model, "查询数据集实体为空");
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
            subWheres.append(columnName).append(" = #{model.").append(field.getName()).append("}");
            index++;
        }

        StringBuilder wheres = new StringBuilder(FIELD_ARCHIVE + " = " + Archive.NORMAL.value());
        if (index > 0) {
            wheres.append(" AND (").append(subWheres).append(")");
        }

        return new SQL() {
            {
                SELECT("*");
                FROM(getTableName());
                WHERE(wheres.toString());
            }
        }.toString();
    }

    /**
     * 判断主键是否为空
     *
     * @param model T
     * @return boolean
     */
    private boolean primaryKeyHasValue(T model) {
        List<BeanProperties> fields = BeanUtils.getFields(model);
        return primaryKeyHasValue(fields);
    }

    /**
     * 判断主键是否为空
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

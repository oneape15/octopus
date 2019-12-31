package com.oneape.octopus.mapper;

import com.oneape.octopus.commons.dto.BeanProperties;
import com.oneape.octopus.commons.value.BeanUtils;
import com.oneape.octopus.model.DO.BaseDO;
import com.oneape.octopus.model.enums.Archive;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSqlProvider<T extends BaseDO> {
    /**
     * 主键字段名称
     */
    private final static String FIELD_PRIMARY_ID = "id";
    /**
     * 创建时间字段名称
     */
    private final static String FIELD_CREATED = "created";
    /**
     * 创建人字段名称
     */
    private final static String FIELD_CREATOR = "creator";
    /**
     * 修改时间字段名称
     */
    private final static String FIELD_MODIFIED = "modified";
    /**
     * 修改人字段名称
     */
    private final static String FIELD_MODIFIER = "modifier";
    /**
     * 归档字段名称
     */
    private final static String FIELD_ARCHIVE = "archive";

    /**
     * 获取表名
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

        Assert.isTrue(model != null, "插入操作实体不能为空");

        List<BeanProperties> fields = BeanUtils.getFields(model);

        String[] columns = new String[fields.size()];
        String[] values = new String[fields.size()];

        int index = 0;
        for (BeanProperties field : fields) {
            columns[index] = field.getName();
            if (StringUtils.equals(field.getName(), FIELD_CREATED)) {
                values[index] = "unix_timestamp(now())";
            } else {
                values[index] = "#{" + field.getDbColumnName() + "}";
            }
            index++;
        }

        Assert.isTrue(fields.size() != 0, "插入数据操作字段为空");

        return new SQL() {
            {
                INSERT_INTO(getTableName());
                INTO_COLUMNS(columns);
                INTO_VALUES(values);
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
        Assert.isTrue(model != null, "更新数据实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        Assert.isTrue(primaryKeyHasValue(fields), "更新时，主键为空");

        List<String> sets = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null &&
                    !StringUtils.equalsAny(field.getName(), FIELD_PRIMARY_ID, FIELD_CREATED, FIELD_CREATOR)) {
                String columnName = field.getDbColumnName();
                if (StringUtils.isBlank(columnName)) {
                    columnName = field.getName();
                }
                sets.add(columnName + " = #{" + field.getName() + "}");
            }
        });

        Assert.isTrue(!sets.isEmpty(), "更新数据操作字段为空");
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
        Assert.isTrue(model != null, "删除操作实体为空");
        Assert.isTrue(primaryKeyHasValue(model), "删除时，主键为空");

        return new SQL() {
            {
                UPDATE(getTableName());
                SET(FIELD_ARCHIVE + "= " + Archive.ARCHIVE,
                        FIELD_MODIFIED + " = unix_timestamp(now())",
                        FIELD_MODIFIER + "#{" + FIELD_MODIFIER + "}");
                WHERE(FIELD_PRIMARY_ID + "= #{" + FIELD_PRIMARY_ID + "}");
            }
        }.toString();
    }

    /**
     * 根据Id查询
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
     * 根据实体中不为null的属性作为查询条件查询
     *
     * @param model T
     * @return String
     */
    public String list(@Param("model") T model) {
        Assert.isTrue(model != null, "查询数据集实体为空");
        List<BeanProperties> fields = BeanUtils.getFields(model);

        List<String> wheres = new ArrayList<>();

        fields.forEach(field -> {
            if (field.getValue() != null) {
                String columnName = field.getDbColumnName();
                if (StringUtils.isBlank(columnName)) {
                    columnName = field.getName();
                }
                wheres.add(columnName + " = #{" + field.getName() + "}");
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

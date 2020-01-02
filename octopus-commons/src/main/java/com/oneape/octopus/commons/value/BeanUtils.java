package com.oneape.octopus.commons.value;

import com.oneape.octopus.commons.dto.BeanProperties;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanUtils {

    public static List<BeanProperties> getFields(Object o) {
        List<BeanProperties> props = new ArrayList<>();
        if (o == null) {
            return props;
        }

        List<Field> fields;
        Class clazz = o.getClass();
        // 当父类为null的时候说明到达了最上层的父类(Object类）
        while (clazz != null) {
            fields = Arrays.asList(clazz.getDeclaredFields());

            fields.forEach(field -> {
                BeanProperties prop = new BeanProperties();

                prop.setName(field.getName());
                prop.setType(field.getType().getName());
                Object value = null;
                try {
                    field.setAccessible(true);
                    value = field.get(o);
                } catch (Exception e) {
                    // nothing to do
                }
                prop.setValue(value);

                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    prop.setDbColumn(true);
                    prop.setNullAble(column.nullable());
                    prop.setDbColumnName(StringUtils.stripToNull(column.name()));
                }

                // 如果没有设置db_column则使用name值
                if (StringUtils.isBlank(prop.getDbColumnName())) {
                    prop.setDbColumnName(prop.getName());
                }
                props.add(prop);
            });

            // 得到父类， 然后赋给自己
            clazz = clazz.getSuperclass();
        }

        return props;
    }
}

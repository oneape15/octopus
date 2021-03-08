package com.oneape.octopus.commons.value;

import com.oneape.octopus.commons.dto.EntityAttribute;
import com.oneape.octopus.commons.enums.EntityColumn;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class EntityColumnUtils {

    /**
     * Gets property information for the object.
     *
     * @param o Object
     * @return List
     */
    public static List<EntityAttribute> getFields(Object o) {
        List<EntityAttribute> props = new ArrayList<>();
        if (o == null) {
            return props;
        }

        List<Field> fields;
        Class<?> clazz = o.getClass();
        // When the parent class is null, the uppermost parent class (Object class) has been reached.
        while (clazz != null) {
            fields = Arrays.asList(clazz.getDeclaredFields());
            for (Field field : fields) {
                EntityAttribute prop = new EntityAttribute();

                if (StringUtils.startsWith(field.getName(), "$")) {
                    continue;
                }
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

                EntityColumn column = field.getAnnotation(EntityColumn.class);
                if (column != null) {
                    prop.setDbColumn(column.dbColumn());
                    prop.setNullable(column.nullable());
                    prop.setColumnName(StringUtils.stripToNull(column.name()));
                    prop.setLength(column.length());
                    prop.setPrecision(column.precision());
                    prop.setScale(column.scale());

                }

                // If 'db_column' is not set, the 'name' value is used
                if (StringUtils.isBlank(prop.getColumnName())) {
                    prop.setColumnName(prop.getName());
                }
                props.add(prop);
            }

            // loop
            clazz = clazz.getSuperclass();
        }

        return props;
    }

    /**
     * Map is converted to the corresponding object.
     *
     * @param map       Map
     * @param beanClass Class
     * @return Object
     * @throws Exception e
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null || map.size() <= 0) return null;

        Object obj = beanClass.newInstance();

        Class<?> oo = obj.getClass();
        List<Class<?>> clazzList = new ArrayList<>();
        do {
            clazzList.add(oo);
            oo = oo.getSuperclass();
        } while (oo != null && oo != Object.class);

        for (Class<?> clz : clazzList) {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                // Sets the value of the corresponding property.
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        }

        return obj;
    }

    /**
     * Object into a Map.
     *
     * @param obj Object
     * @return Map
     * @throws Exception e
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) return null;

        Class<?> oo = obj.getClass();
        List<Class<?>> clazzList = new ArrayList<>();
        do {
            clazzList.add(oo);
            oo = oo.getSuperclass();
        } while (oo != null && oo != Object.class);
        Map<String, Object> map = new HashMap<>();

        for (Class<?> clz : clazzList) {
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                int mod = field.getModifiers();

                // Filter static and final types.
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        }

        return map;
    }
}

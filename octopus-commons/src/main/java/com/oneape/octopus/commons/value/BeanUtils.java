package com.oneape.octopus.commons.value;

import com.oneape.octopus.commons.dto.BeanProperties;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class BeanUtils {

    /**
     * 获取对象的属性信息
     *
     * @param o Object
     * @return List
     */
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
                    prop.setNullable(column.nullable());
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

    /**
     * Map转换成相应对象
     *
     * @param map       Map
     * @param beanClass Class
     * @return Object
     * @throws Exception e
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null || map.size() <= 0) return null;

        Object obj = beanClass.newInstance();

        //获取关联的所有类，本类以及所有父类
        Class oo = obj.getClass();
        List<Class> clazzs = new ArrayList<>();
        while (true) {
            clazzs.add(oo);
            oo = oo.getSuperclass();
            if (oo == null || oo == Object.class) break;
        }

        for (Class clz : clazzs) {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                // 设置对应属性的值
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        }

        return obj;
    }

    /**
     * 对象转换成Map
     *
     * @param obj Object
     * @return Map
     * @throws Exception e
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) return null;

        // 获取关联的所有类, 本类及所有父类
        Class oo = obj.getClass();
        List<Class> clazzs = new ArrayList<>();
        while (true) {
            clazzs.add(oo);
            oo = oo.getSuperclass();
            if (oo == null || oo == Object.class) break;
        }
        Map<String, Object> map = new HashMap<>();

        for (Class clz : clazzs) {
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field field : declaredFields) {
                int mod = field.getModifiers();

                //过滤 static 和 final 类型
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

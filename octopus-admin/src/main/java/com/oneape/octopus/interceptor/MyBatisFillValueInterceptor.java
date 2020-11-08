package com.oneape.octopus.interceptor;

import com.oneape.octopus.commons.annotation.AutoUniqueId;
import com.oneape.octopus.commons.annotation.Creator;
import com.oneape.octopus.commons.annotation.Modifier;
import com.oneape.octopus.commons.annotation.SortId;
import com.oneape.octopus.commons.constant.OctopusConstant;
import com.oneape.octopus.config.ApplicationContextProvider;
import com.oneape.octopus.service.system.AccountService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MyBatisFillValueInterceptor implements Interceptor {

    private AccountService      accountService;
    private UIDGeneratorService uidGeneratorService;

    private synchronized Long getUserId() {
        if (accountService == null) {
            synchronized (MyBatisFillValueInterceptor.class) {
                accountService = ApplicationContextProvider.getBean(AccountService.class);
            }
        }
        if (accountService != null) {
            return accountService.getCurrentUserId();
        }
        return OctopusConstant.SYS_USER;
    }

    private synchronized Long getUid() {
        if (uidGeneratorService == null) {
            synchronized (MyBatisFillValueInterceptor.class) {
                uidGeneratorService = ApplicationContextProvider.getBean(UIDGeneratorService.class);
            }
        }
        if (uidGeneratorService != null) {
            return uidGeneratorService.getUid();
        }
        return OctopusConstant.SYS_USER;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // get the sql command type
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // get params
        Object param = invocation.getArgs()[1];

        // Gets the private member variable
        Field[] declaredFields = getAllFields(param);
        Long autoId = null;
        for (Field field : declaredFields) {
            // insert data
            if (sqlCommandType.equals(SqlCommandType.INSERT)) {
                if (field.getAnnotation(AutoUniqueId.class) != null) {
                    // set the unique id.
                    field.setAccessible(true);
                    if (autoId == null) {
                        autoId = getUid();
                    }
                    field.set(param, autoId);
                }
                if (field.getAnnotation(SortId.class) != null) {
                    // set sort id value.
                    field.setAccessible(true);
                    Object value = getFieldValueByName(field.getName(), param);
                    if (autoId == null) {
                        autoId = getUid();
                    }
                    if (value == null) {
                        field.set(param, autoId);
                    }
                }
                // Set creator information
                if (field.getAnnotation(Creator.class) != null) {
                    field.setAccessible(true);
                    Object o = field.get(param);
                    if (o == null) {
                        field.set(param, getUserId());
                    }
                }
            }

            // update data
            if (sqlCommandType.equals(SqlCommandType.UPDATE)) {
                // Set modifier information
                if (field.getAnnotation(Modifier.class) != null) {
                    field.setAccessible(true);
                    field.set(param, getUserId());
                }
            }
        }

        return invocation.proceed();
    }


    /**
     * get all field ( current class, supper class )
     *
     * @param object Object
     * @return Filed[]
     */
    private static Field[] getAllFields(Object object) {
        if (object == null) return new Field[0];

        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    private static Object getFieldValueByName(String name, Object object) throws Exception {
        String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        Method method;
        method = object.getClass().getMethod(getter, new Class[]{});
        return method.invoke(object);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}

package com.oneape.octopus.interceptor;

import com.oneape.octopus.annotation.AutoUniqueId;
import com.oneape.octopus.annotation.Creator;
import com.oneape.octopus.annotation.Modifier;
import com.oneape.octopus.annotation.SortId;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.config.ApplicationContextProvider;
import com.oneape.octopus.service.system.AccountService;
import com.oneape.octopus.service.uid.UIDGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class AutoSetValueInterceptor implements Interceptor {

    private AccountService      accountService;
    private UIDGeneratorService uidGeneratorService;

    private synchronized Long getUserId() {
        if (accountService == null) {
            synchronized (AutoSetValueInterceptor.class) {
                accountService = ApplicationContextProvider.getBean(AccountService.class);
            }
        }
        if (accountService != null) {
            return accountService.getCurrentUserId();
        }
        return GlobalConstant.SYS_USER;
    }

    private synchronized Long getUid() {
        if (uidGeneratorService == null) {
            synchronized (AutoSetValueInterceptor.class) {
                uidGeneratorService = ApplicationContextProvider.getBean(UIDGeneratorService.class);
            }
        }
        if (uidGeneratorService != null) {
            return uidGeneratorService.getUid();
        }
        return -1L;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // get the sql command type
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // get params
        Object param = invocation.getArgs()[1];

        // 获取私有成员变量
        Field[] declaredFields = getAllFields(param);
        Long autoId = null;
        for (Field field : declaredFields) {
            // insert data
            if (sqlCommandType.equals(SqlCommandType.INSERT)) {
                if (field.getAnnotation(AutoUniqueId.class) != null) {
                    // set the unique id.
                    field.setAccessible(true);
                    autoId = getUid();
                    field.set(param, autoId);
                }
                if (field.getAnnotation(SortId.class) != null) {
                    // set sort id value.
                    field.setAccessible(true);
                    if (autoId == null) {
                        autoId = getUid();
                    }
                    field.set(param, autoId);
                }
                // 设置创建者信息
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
                // 设置修改者信息
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

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}

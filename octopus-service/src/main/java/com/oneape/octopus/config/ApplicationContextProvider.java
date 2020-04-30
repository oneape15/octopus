package com.oneape.octopus.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    @Override
    public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
        this.appCtx = appCtx;
    }

    public static ApplicationContext getAppCtx() {
        return appCtx;
    }

    /**
     * 通过name获取Bean.
     *
     * @param name String
     * @return Object
     */
    public static Object getBean(String name) {
        if (getAppCtx() != null) {
            return getAppCtx().getBean(name);
        }
        return null;
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz Class
     * @param <T>   T
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        if (getAppCtx() != null) {
            return getAppCtx().getBean(clazz);
        }
        return null;
    }

    /**
     * 通过name, 以及clazz返回指定的Bean.
     *
     * @param name  String
     * @param clazz Class
     * @param <T>   T
     * @return T
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        if (getAppCtx() != null) {
            return getAppCtx().getBean(name, clazz);
        }
        return null;
    }

    /**
     * 发布容器事件
     */
    public static void publishEvent(ApplicationEvent applicationEvent) {
        if (appCtx != null) {
            appCtx.publishEvent(applicationEvent);
        }
    }
}

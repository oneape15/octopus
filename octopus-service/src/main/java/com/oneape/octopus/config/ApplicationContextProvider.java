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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appCtx = applicationContext;
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
        return getAppCtx().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz Class
     * @param <T>   T
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        return getAppCtx().getBean(clazz);
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
        return getAppCtx().getBean(name, clazz);
    }

    /**
     * 发布容器事件
     *
     * @param applicationEvent
     */
    public static void publishEvent(ApplicationEvent applicationEvent) {
        if (appCtx != null) {
            appCtx.publishEvent(applicationEvent);
        }
    }
}

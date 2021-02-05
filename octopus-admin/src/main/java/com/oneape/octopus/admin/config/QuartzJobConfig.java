package com.oneape.octopus.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 10:37.
 * Modify:
 */
@Slf4j
@ComponentScan(basePackages = "com.oneape.octopus.admin.job")
@Configuration
public class QuartzJobConfig {
    @Resource
    private DataSource                 dataSource;
    @Resource
    private PlatformTransactionManager txManager;

    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // Set automatically start the scheduler after initialization.
        factory.setAutoStartup(true);
        factory.setConfigLocation(new ClassPathResource("/spring-quartz.properties"));
        factory.setJobFactory(new SpringBeanJobFactory());
        factory.setDataSource(dataSource);
        factory.setTransactionManager(txManager);
        factory.setOverwriteExistingJobs(true);

        return factory;
    }

    @Bean
    public Scheduler scheduler() {
        return quartzScheduler().getScheduler();
    }
}

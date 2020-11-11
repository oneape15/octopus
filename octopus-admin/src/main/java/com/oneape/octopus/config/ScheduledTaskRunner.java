package com.oneape.octopus.config;

import com.oneape.octopus.service.task.QuartzTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Start tasks that need to be self-starting after the project is started.
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 14:51.
 * Modify:
 */
@Slf4j
@Order(1)
@Component
public class ScheduledTaskRunner implements ApplicationRunner {
    @Resource
    private QuartzTaskService quartzTaskService;

    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Start the task -- start! ");
        quartzTaskService.initInvokeTask();
        log.info("Start the task -- end! ");
    }
}

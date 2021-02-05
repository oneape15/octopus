package com.oneape.octopus.admin.job;

import com.oneape.octopus.admin.config.ApplicationContextProvider;
import com.oneape.octopus.admin.service.task.QuartzTaskService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2021-01-12 15:30.
 * Modify:
 */
@Slf4j
@Setter
public class QuartzScheduledJob implements Job {

    private Long id;

    private QuartzTaskService quartzTaskService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (quartzTaskService == null) {
            quartzTaskService = ApplicationContextProvider.getBean(QuartzTaskService.class);
        }

        if (quartzTaskService == null) {
            log.error("Run quartz task fail, the QuartzTaskService uninitialized.");
            return;
        }

        int status = quartzTaskService.executeTaskById(id);
        if (status > 0) {
            log.info("The task {} execute success.", id);
        } else {
            log.error("The task {} execute fail.", id);
        }
    }
}

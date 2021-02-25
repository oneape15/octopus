package com.oneape.octopus.admin.service.task.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.mapper.task.QuartzTaskMapper;
import com.oneape.octopus.admin.service.task.QuartzTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 11:09.
 * Modify:
 */
@Slf4j
@Service
public class QuartzTaskServiceImpl implements QuartzTaskService {

    @Resource
    private QuartzTaskMapper quartzTaskMapper;
    @Resource
    private Scheduler        quartzScheduler;

    /**
     * Init all invoke task.
     */
    @Override
    public void initInvokeTask() {
        List<QuartzTaskDO> taskList = quartzTaskMapper.getAllInvokeTask();
        if (CollectionUtils.isEmpty(taskList)) {
            log.info("There are no invoke tasks!");
            return;
        }

        int count = 0;
        for (QuartzTaskDO task : taskList) {
            count++;
            this.addJob2Schedule(task, buildJobDataMap(task));
        }

        log.info("Total startup user tasks: {}.", count);
    }

    private Map<String, Object> buildJobDataMap(QuartzTaskDO taskDO) {
        Map<String, Object> jobDataMap = new HashMap<>();
        jobDataMap.put("id", taskDO.getId());

        return jobDataMap;
    }

    /**
     * save data to table.
     * <p>
     * If the Model property ID is not null, the update operation is performed, or the insert operation is performed。
     *
     * @param model T
     * @return int 1 - success; 0 - fail.
     */
    @Transactional
    @Override
    public int save(QuartzTaskDO model) {
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("task.info.null"));
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getTaskName()),
                I18nMsgConfig.getMessage("task.name.empty"));
        Preconditions.checkArgument(StringUtils.isNotBlank(model.getCron()),
                I18nMsgConfig.getMessage("task.cron.empty"));
        Preconditions.checkArgument(CronExpression.isValidExpression(model.getCron()),
                I18nMsgConfig.getMessage("task.cron.illegal"));
        Preconditions.checkArgument(
                (model.getStatus() != null) && (model.getStatus() == 0 || model.getStatus() == 1),
                I18nMsgConfig.getMessage("task.status.invalid"));

        boolean isEdit = model.getId() != null;
        QuartzTaskDO oldTaskDO = null;
        if (isEdit) {
            oldTaskDO = Preconditions.checkNotNull(
                    quartzTaskMapper.findById(model.getId()),
                    I18nMsgConfig.getMessage("task.id.invalid"));
        }

        // Checks if the task name already exists.
        Preconditions.checkArgument(
                quartzTaskMapper.hasSameTaskName(model.getTaskName(), isEdit ? model.getId() : null) <= 0,
                I18nMsgConfig.getMessage("task.name.exist"));

        if (StringUtils.isBlank(model.getGroupName())) {
            model.setGroupName("DEFAULT_GROUP");
        }

        int status;

        Map<String, Object> jobDataMap = buildJobDataMap(model);

        // update task information.
        if (isEdit) {
            status = quartzTaskMapper.update(model);

            // Update scheduling task.
            if (status > 0) {
                // Depending on the status comparison between the old and new status, the scheduling task is added, updated, or deleted.
                Integer oldStatus = oldTaskDO.getStatus();
                Integer newStatus = model.getStatus();
                // Same, and the status is 1, just update.
                if (oldStatus.equals(newStatus) && oldStatus == 1) {
                    updateJob2Schedule(model, jobDataMap);
                } else {
                    // Different, depending on the new state.
                    if (newStatus == 1) {
                        addJob2Schedule(model, jobDataMap);
                    } else {
                        deleteJob2Schedule(model);
                    }
                }
            }
        } else {
            // add new task.
            status = quartzTaskMapper.insert(model);

            // The task is added to the schedule only if the save is successful and the task is enabled.
            if (status > 0 && model.getStatus() == 1) {
                addJob2Schedule(model, jobDataMap);
            }
        }

        return status;
    }

    /**
     * run one time.
     *
     * @param taskId Long
     * @return int 1 - success; 0 - fail;
     */
    @Override
    public int runOnce(Long taskId) {
        QuartzTaskDO taskDO = Preconditions.checkNotNull(quartzTaskMapper.findById(taskId),
                I18nMsgConfig.getMessage("task.id.invalid"));
        JobKey jobKey = JobKey.jobKey(taskDO.getTaskName(), taskDO.getGroupName());
        try {
            // Get the JobDataMap，write the data.
            Map<String, Object> paramMap = buildJobDataMap(taskDO);
            JobDataMap dataMap = new JobDataMap();
            dataMap.putAll(paramMap);

            quartzScheduler.triggerJob(jobKey, dataMap);
            return 1;
        } catch (Exception e) {
            throw new BizException("Trigger job fail: " + e.getMessage());
        }
    }

    /**
     * Delete by primary key Id.
     *
     * @param id Long
     * @return int 1 - success; 0 - fail.
     */
    @Override
    public int deleteById(Long id) {
        QuartzTaskDO taskDO = Preconditions.checkNotNull(quartzTaskMapper.findById(id),
                I18nMsgConfig.getMessage("task.id.invalid"));
        int status = quartzTaskMapper.delete(taskDO);

        // Delete scheduling tasks.
        if (status > 0) {
            deleteJob2Schedule(taskDO);
        }

        return status;
    }

    /**
     * update task information.
     *
     * @param taskId Long
     * @param status Integer 0 - block up; 1 - invoke;
     * @return int 1 - success; 0 - fail;
     */
    @Override
    public int updateTaskStatus(Long taskId, Integer status) {
        QuartzTaskDO taskDO = Preconditions.checkNotNull(quartzTaskMapper.findById(taskId),
                I18nMsgConfig.getMessage("task.id.invalid"));
        Preconditions.checkArgument(
                status != null && (status == 0 || status == 1),
                I18nMsgConfig.getMessage("task.status.invalid"));

        if (Integer.compare(taskDO.getStatus(), status) == 0) {
            return 1;
        }
        taskDO.setStatus(status);

        return save(taskDO);
    }

    /**
     * Query against an object.
     *
     * @param model T
     * @return List
     */
    @Override
    public List<QuartzTaskDO> find(QuartzTaskDO model) {
        Preconditions.checkNotNull(model, I18nMsgConfig.getMessage("task.info.null"));
        return quartzTaskMapper.list(model);
    }

    /**
     * Get task information based on Id
     *
     * @param taskId Long
     * @return QuartzTaskDO
     */
    @Override
    public QuartzTaskDO findById(Long taskId) {
        Preconditions.checkNotNull(taskId, I18nMsgConfig.getMessage("task.id.invalid"));
        return quartzTaskMapper.findById(taskId);
    }

    /**
     * Add the task to the schedule.
     *
     * @param taskDO     QuartzTaskDO
     * @param jobDataMap Map
     */
    @Override
    public void addJob2Schedule(QuartzTaskDO taskDO, Map<String, Object> jobDataMap) {
        Preconditions.checkNotNull(taskDO, I18nMsgConfig.getMessage("task.info.null"));
        Preconditions.checkArgument(!StringUtils.isAnyBlank(taskDO.getGroupName(), taskDO.getTaskName()), "The task name or task group is empty!");

        String taskName = taskDO.getTaskName();
        String taskGroup = taskDO.getGroupName();
        String cron = taskDO.getCron();

        TriggerKey triggerKey = TriggerKey.triggerKey(taskName, taskGroup);
        try {
            boolean status = quartzScheduler.checkExists(triggerKey);
            log.info("checkExists quartTaskName: {}, status: {}", taskName, status);

            // Job already exists, return it directly.
            if (status) return;

            Class<?> clazz = Class.forName(taskDO.getJobClass());

            JobDetail jobDetail = JobBuilder
                    .newJob(((Job) clazz.newInstance()).getClass())
                    .withIdentity(taskName, taskGroup)
                    .build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(cron)
                    .withMisfireHandlingInstructionDoNothing();

            // Build a new trigger based on the new cronExpression expression.
            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .startNow()
                    .withIdentity(taskName, taskGroup)
                    .withSchedule(scheduleBuilder)
                    .build();

            // Get the JobDataMap，write the data.
            trigger.getJobDataMap().putAll(jobDataMap);

            quartzScheduler.scheduleJob(jobDetail, trigger);
            log.info("Add task : {}-{}({}) success!", taskGroup, taskName, cron);
        } catch (Exception e) {
            log.error("Add task : {}-{}({}) fail! msg: {}", taskGroup, taskName, cron, e);
            throw new BizException(I18nMsgConfig.getMessage("task.add.fail", taskGroup + "-" + taskName + "(" + cron + ")"), e);
        }
    }

    /**
     * Update task.
     *
     * @param taskDO     QuartzTaskDO
     * @param jobDataMap Map
     */
    @Override
    public void updateJob2Schedule(QuartzTaskDO taskDO, Map<String, Object> jobDataMap) {
        Preconditions.checkNotNull(taskDO, I18nMsgConfig.getMessage("task.info.null"));
        Preconditions.checkArgument(!StringUtils.isAnyBlank(taskDO.getGroupName(), taskDO.getTaskName()), "The task name or task group is empty!");

        String taskName = taskDO.getTaskName();
        String taskGroup = taskDO.getGroupName();
        String cron = taskDO.getCron();

        TriggerKey triggerKey = TriggerKey.triggerKey(taskName, taskGroup);

        try {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(cron)
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger;
            if (quartzScheduler.getTrigger(triggerKey) == null) {
                trigger = TriggerBuilder
                        .newTrigger()
                        .startNow()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .build();
            } else {
                trigger = (CronTrigger) quartzScheduler.getTrigger(triggerKey);

                trigger = trigger.getTriggerBuilder()
                        .startNow()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .build();
            }

            // Get the JobDataMap，write the data.
            trigger.getJobDataMap().putAll(jobDataMap);

            quartzScheduler.rescheduleJob(triggerKey, trigger);

            log.info("Update task : {}-{}({}) success!", taskGroup, taskName, cron);
        } catch (Exception e) {
            log.error("Update task : {}-{}({}) fail! msg: {}", taskGroup, taskName, cron, e);
            throw new BizException(I18nMsgConfig.getMessage("task.update.fail", taskGroup + "-" + taskName + "(" + cron + ")"), e);
        }
    }

    /**
     * Remove task.
     *
     * @param taskDO QuartzTaskDO
     */
    @Override
    public void deleteJob2Schedule(QuartzTaskDO taskDO) {
        Preconditions.checkNotNull(taskDO, I18nMsgConfig.getMessage("task.info.null"));
        Preconditions.checkArgument(!StringUtils.isAnyBlank(taskDO.getGroupName(), taskDO.getTaskName()), "The task name or task group is empty!");

        TriggerKey triggerKey = TriggerKey.triggerKey(taskDO.getTaskName(), taskDO.getGroupName());

        try {
            quartzScheduler.pauseTrigger(triggerKey);
            quartzScheduler.unscheduleJob(triggerKey);
            quartzScheduler.deleteJob(JobKey.jobKey(taskDO.getTaskName(), taskDO.getGroupName()));
            log.info("Remove task: {} success!", JSON.toJSONString(triggerKey));
        } catch (SchedulerException e) {
            log.error("Remove task : {}-{}({}) fail! msg: {}", JSON.toJSONString(taskDO), e);
            throw new BizException(
                    I18nMsgConfig.getMessage("task.remove.fail", taskDO.getGroupName() + "-" + taskDO.getTaskName() + "(" + taskDO.getCron() + ")"),
                    e);
        }
    }

    /**
     * Execute the task according to the ID.
     *
     * @param taskId Long
     * @return int 1 - success; 0 - fail;
     */
    @Override
    public int executeTaskById(Long taskId) {
        Preconditions.checkNotNull(quartzTaskMapper.findById(taskId), I18nMsgConfig.getMessage("task.id.invalid"));
        int status = 0;

        // success, update the last run time
        if (status > 0) {
            QuartzTaskDO modal = new QuartzTaskDO();
            modal.setId(taskId);
            modal.setLastRunTime(System.currentTimeMillis());
            quartzTaskMapper.update(modal);
        }
        return status;
    }
}

package com.oneape.octopus.service.task;


import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.service.BaseService;

import java.util.Map;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 11:09.
 * Modify:
 */
public interface QuartzTaskService extends BaseService<QuartzTaskDO> {

    /**
     * Init all invoke task.
     */
    void initInvokeTask();

    /**
     * run one time.
     *
     * @param taskId Long
     * @return int 1 - success; 0 - fail;
     */
    int runOnce(Long taskId);

    /**
     * update task information.
     *
     * @param taskId Long
     * @param status Integer 0 - block up; 1 - invoke;
     * @return int 1 - success; 0 - fail;
     */
    int updateTaskStatus(Long taskId, Integer status);

    /**
     * Add the task to the schedule.
     *
     * @param taskDO     QuartzTaskDO
     * @param jobDataMap Map
     */
    void addJob2Schedule(QuartzTaskDO taskDO, Map<String, Object> jobDataMap);

    /**
     * Update task.
     *
     * @param taskDO     QuartzTaskDO
     * @param jobDataMap Map
     */
    void updateJob2Schedule(QuartzTaskDO taskDO, Map<String, Object> jobDataMap);

    /**
     * Remove task.
     *
     * @param taskDO QuartzTaskDO
     */
    void deleteJob2Schedule(QuartzTaskDO taskDO);

    /**
     * Execute the task according to the ID.
     *
     * @param taskId Long
     * @return int 1 - success; 0 - fail;
     */
    int executeTaskById(Long taskId);

}

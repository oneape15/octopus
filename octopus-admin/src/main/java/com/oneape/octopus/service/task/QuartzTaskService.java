package com.oneape.octopus.service.task;


import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.service.BaseService;

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
     * @param status Integer 0 - 停用, 1 - 启用
     * @return int 1 - success; 0 - fail;
     */
    int updateTaskStatus(Long taskId, Integer status);

    /**
     * Get task information based on Id
     *
     * @param taskId Long
     * @return QuartzTaskDO
     */
    QuartzTaskDO findById(Long taskId);

}

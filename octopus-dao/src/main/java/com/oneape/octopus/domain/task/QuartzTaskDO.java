package com.oneape.octopus.domain.task;

import com.oneape.octopus.commons.annotation.EntityColumn;
import com.oneape.octopus.domain.BaseDO;
import com.oneape.octopus.dto.task.QuartzTaskParamDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * quartz任务自定义对象类
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 10:58.
 * Modify:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuartzTaskDO extends BaseDO {
    /**
     * The task name
     */
    @EntityColumn(name = "task_name")
    private String taskName;
    /**
     * task group name
     */
    @EntityColumn(name = "group_name")
    private String groupName;
    /**
     * The class in which the job resides
     */
    @EntityColumn(name = "job_class")
    private String jobClass;
    /**
     * Cron expression
     */
    private String cron;
    /**
     * Run task parameters (JSON format)
     *
     * @see QuartzTaskParamDTO
     */
    private String params;
    /**
     * status 0 - block up; 1 - invoke
     */
    private Integer status;
    /**
     * Last run time
     */
    @EntityColumn(name = "last_run_time")
    private Long lastRunTime;
    /**
     * The task own user id.
     */
    @EntityColumn(name = "owner_id")
    private Long ownerId;

}

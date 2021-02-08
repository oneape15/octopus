package com.oneape.octopus.admin.controller.task.from;

import com.oneape.octopus.admin.controller.BaseForm;
import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.dto.task.QuartzTaskParamDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-09-23 16:18.
 * Modify:
 */
@Data
public class TaskForm extends BaseForm {
    @NotNull(message = "{TaskForm.NotNull.id}", groups = {EditCheck.class})
    private Long               id;
    @NotBlank(message = "{TaskForm.NotBlank.taskName}", groups = {AddCheck.class, EditCheck.class})
    private String             taskName;
    private String             jobClass;
    @NotBlank(message = "{TaskForm.NotBlank.cron}", groups = {AddCheck.class, EditCheck.class})
    private String             cron;
    // status 0 - block up; 1 - invoke
    private Integer            status;
    // The task parameters.
    @NotNull(message = "{TaskForm.NotNull.params}", groups = {AddCheck.class})
    private QuartzTaskParamDTO params;

    public interface AddCheck {
    }

    public interface EditCheck {
    }

    public QuartzTaskDO toDO() {
        QuartzTaskDO taskDO = new QuartzTaskDO();
        taskDO.setId(id);
        taskDO.setTaskName(taskName);
        taskDO.setJobClass(jobClass);
        taskDO.setCron(cron);
        taskDO.setStatus(status == null ? 0 : status);

        return taskDO;
    }
}

package com.oneape.octopus.admin.controller.task;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.controller.task.from.TaskForm;
import com.oneape.octopus.admin.job.QuartzScheduledJob;
import com.oneape.octopus.admin.service.serve.ServeInfoService;
import com.oneape.octopus.admin.service.task.QuartzTaskService;
import com.oneape.octopus.admin.service.warehouse.DatasourceService;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.dto.ApiResult;
import com.oneape.octopus.commons.enums.QuartzTaskType;
import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.dto.task.QuartzTaskParamDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-11-11 19:51.
 * Modify:
 */
@Slf4j
@RequestMapping("/task")
@RestController
@Api(value = "/task", tags = "Task opt api")
public class QuartzTaskController {

    @Resource
    private QuartzTaskService quartzTaskService;
    @Resource
    private DatasourceService datasourceService;
    @Resource
    private ServeInfoService serveInfoService;

    /**
     * Get task information list.
     */
    @PostMapping(value = "/list")
    public ApiResult<PageInfo<QuartzTaskDO>> listTask(@RequestBody TaskForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        List<QuartzTaskDO> vos = quartzTaskService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * Save task information.
     */
    @PostMapping(value = "/save")
    public ApiResult<String> saveTask(@RequestBody @Validated(value = TaskForm.AddCheck.class) TaskForm form) {
        int status = quartzTaskService.save(formatTaskDO(form));
        if (status > 0) {
            return ApiResult.ofMessage(I18nMsgConfig.getMessage("task.save.success"));
        }
        return ApiResult.ofError(-1, I18nMsgConfig.getMessage("task.save.fail"));
    }

    /**
     * Delete the task by task id.
     *
     * @param taskId Long
     */
    @PostMapping(value = "/delete/{taskId}")
    public ApiResult<String> deleteTask(@PathVariable(name = "taskId") Long taskId) {
        int status = quartzTaskService.deleteById(taskId);
        if (status > 0) {
            return ApiResult.ofMessage(I18nMsgConfig.getMessage("task.del.success"));
        }
        return ApiResult.ofError(-1, I18nMsgConfig.getMessage("task.del.fail"));
    }

    /**
     * Get the task information by task id.
     *
     * @param taskId Long
     */
    @PostMapping(value = "/get/{taskId}")
    public ApiResult<QuartzTaskDO> listTask(@PathVariable(name = "taskId") Long taskId) {
        return ApiResult.ofData(quartzTaskService.findById(taskId));
    }

    /**
     * Run a task for once.
     *
     * @param taskId Long
     */
    @PostMapping(value = "/runOnce/{taskId}")
    public ApiResult<String> runOnceTask(@PathVariable(name = "taskId") Long taskId) {
        int status = quartzTaskService.runOnce(taskId);
        if (status > 0) {
            return ApiResult.ofMessage(I18nMsgConfig.getMessage("task.run.success"));
        }
        return ApiResult.ofError(-1, I18nMsgConfig.getMessage("task.run.fail"));
    }

    /**
     * Change task status.
     *
     * @param taskId Long
     * @param status Integer
     */
    @PostMapping(value = "/changeStatus/{taskId}/{status}")
    public ApiResult<String> changeTaskStatus(@PathVariable(name = "taskId") Long taskId, @PathVariable(name = "status") Integer status) {
        int retStatus = quartzTaskService.updateTaskStatus(taskId, status);
        if (retStatus > 0) {
            return ApiResult.ofMessage(I18nMsgConfig.getMessage("task.changeStatus.success"));
        }
        return ApiResult.ofError(-1, I18nMsgConfig.getMessage("task.changeStatus.fail"));
    }

    /**
     * Assemble task objects
     *
     * @param form TaskForm
     * @return QuartzTaskDO
     */
    private QuartzTaskDO formatTaskDO(TaskForm form) {
        QuartzTaskDO taskDO = form.toDO();
        if (form.getParams() != null) {
            QuartzTaskParamDTO paramDTO = form.getParams();
            String runType = paramDTO.getRunType();
            Preconditions.checkArgument(StringUtils.isNotBlank(runType), I18nMsgConfig.getMessage("task.runType.empty"));
            runType = StringUtils.upperCase(runType);
            if (!QuartzTaskType.isValidType(runType)) {
                throw new BizException(I18nMsgConfig.getMessage("task.runType.invalid"));
            }
            paramDTO.setRunType(runType);
            if (StringUtils.isBlank(form.getJobClass())) {
                taskDO.setJobClass(QuartzScheduledJob.class.getName());
            }

            QuartzTaskType taskType = QuartzTaskType.getByName(runType);
            if (taskType == QuartzTaskType.RAW_SQL) {
                Preconditions.checkArgument(
                        CollectionUtils.isNotEmpty(datasourceService.findByName(paramDTO.getDsName())),
                        I18nMsgConfig.getMessage("task.dsName.invalid"));
                Preconditions.checkArgument(StringUtils.isNotBlank(paramDTO.getRawSql()));
            } else if (taskType == QuartzTaskType.SERVE) {
                Preconditions.checkNotNull(
                        serveInfoService.findById(paramDTO.getServeId()),
                        I18nMsgConfig.getMessage("serve.id.invalid"));
            }

            Preconditions.checkArgument(
                    CollectionUtils.isNotEmpty(paramDTO.getEmailTos()),
                    I18nMsgConfig.getMessage("global.mailbox.invalid"));

            taskDO.setParams(JSON.toJSONString(paramDTO));
        }
        return taskDO;
    }
}

package com.oneape.octopus.controller.task;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.oneape.octopus.commons.cause.BizException;
import com.oneape.octopus.commons.enums.QuartzTaskType;
import com.oneape.octopus.controller.task.from.TaskForm;
import com.oneape.octopus.domain.task.QuartzTaskDO;
import com.oneape.octopus.dto.task.QuartzTaskParamDTO;
import com.oneape.octopus.job.QuartzScheduledJob;
import com.oneape.octopus.model.vo.ApiResult;
import com.oneape.octopus.service.schema.DatasourceService;
import com.oneape.octopus.service.serve.ServeInfoService;
import com.oneape.octopus.service.task.QuartzTaskService;
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
    private ServeInfoService  serveInfoService;

    @PostMapping(value = "/list")
    public ApiResult listTask(@RequestBody TaskForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        List<QuartzTaskDO> vos = quartzTaskService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    @PostMapping(value = "/save")
    public ApiResult saveTask(@RequestBody @Validated(value = TaskForm.AddCheck.class) TaskForm form) {
        int status = quartzTaskService.save(formatTaskDO(form));
        if (status > 0) {
            return ApiResult.ofMessage("Saved task information successfully.");
        }
        return ApiResult.ofError(-1, "Saved task information fail.");
    }

    @PostMapping(value = "/delete/{taskId}")
    public ApiResult deleteTask(@PathVariable(name = "taskId") Long taskId) {
        int status = quartzTaskService.deleteById(taskId);
        if (status > 0) {
            return ApiResult.ofMessage("Deleted task information successfully.");
        }
        return ApiResult.ofError(-1, "Deleted task information fail.");
    }

    @PostMapping(value = "/get/{taskId}")
    public ApiResult listTask(@PathVariable(name = "taskId") Long taskId) {
        return ApiResult.ofData(quartzTaskService.findById(taskId));
    }

    @PostMapping(value = "/runOnce/{taskId}")
    public ApiResult runOnceTask(@PathVariable(name = "taskId") Long taskId) {
        int status = quartzTaskService.runOnce(taskId);
        if (status > 0) {
            return ApiResult.ofMessage("Run task successful.");
        }
        return ApiResult.ofError(-1, "Run task fail.");
    }

    @PostMapping(value = "/changeStatus/{taskId}/{status}")
    public ApiResult changeTaskStatus(@PathVariable(name = "taskId") Long taskId, @PathVariable(name = "status") Integer status) {
        int retStatus = quartzTaskService.updateTaskStatus(taskId, status);
        if (retStatus > 0) {
            return ApiResult.ofMessage("Task status update successful.");
        }
        return ApiResult.ofError(-1, "Task status update fail.");
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
            Preconditions.checkArgument(StringUtils.isNotBlank(runType), "The task run type is empty.");
            runType = StringUtils.upperCase(runType);
            if (!QuartzTaskType.isValidType(runType)) {
                throw new BizException("The run type value should be one of [SERVE, RAW_SQL, SIMPLE_EMAIL].");
            }
            paramDTO.setRunType(runType);
            if (StringUtils.isBlank(form.getJobClass())) {
                taskDO.setJobClass(QuartzScheduledJob.class.getName());
            }

            QuartzTaskType taskType = QuartzTaskType.getByName(runType);
            if (taskType == QuartzTaskType.RAW_SQL) {
                Preconditions.checkArgument(
                        CollectionUtils.isNotEmpty(datasourceService.findByName(paramDTO.getDsName())),
                        "The data source name field dsName is invalid."
                );
                Preconditions.checkArgument(StringUtils.isNotBlank(paramDTO.getRawSql()));
            } else if (taskType == QuartzTaskType.SERVE) {
                Preconditions.checkNotNull(serveInfoService.findById(paramDTO.getServeId()), "The serveId field is invalid.");
            }

            Preconditions.checkArgument(CollectionUtils.isNotEmpty(paramDTO.getEmailTos()), "Mailbox access is empty.");

            taskDO.setParams(JSON.toJSONString(paramDTO));
        }
        return taskDO;
    }
}

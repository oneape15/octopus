package com.oneape.octopus.admin.controller.system;

import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.commons.value.MaskUtils;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.dto.Pair;
import com.oneape.octopus.admin.controller.system.form.ResForm;
import com.oneape.octopus.commons.dto.ApiResult;
import com.oneape.octopus.admin.service.system.ResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * System resource management.
 */
@RestController
@RequestMapping("/res")
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @PostMapping("/save")
    public ApiResult<String> doSaveRes(@RequestBody @Validated(value = ResForm.AddCheck.class) ResForm form) {
        int status = resourceService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("resource.save.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("resource.save.fail"));
    }

    @PostMapping("/del/{resId}")
    public ApiResult<String> doDelRes(@PathVariable(name = "resId") Long resId) {
        int status = resourceService.deleteById(resId);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("resource.del.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("resource.del.fail"));
    }

    /**
     * Gets all permission types.
     */
    @PostMapping("/allMask")
    public ApiResult<List<Pair<Integer, String>>> getAllMask() {
        return ApiResult.ofData(MaskUtils.allMask());
    }
}

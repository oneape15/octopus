package com.oneape.octopus.controller.system;

import com.oneape.octopus.common.MaskUtils;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.system.form.ResForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.service.system.ResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * System resource management.
 */
@RestController
@RequestMapping("/res")
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @PostMapping("/add")
    public ApiResult<String> doAddRes(@RequestBody @Validated(value = ResForm.AddCheck.class) ResForm form) {
        int status = resourceService.insert(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Add resource successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Add resource fail");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditRes(@RequestBody @Validated(value = ResForm.EditCheck.class) ResForm form) {
        int status = resourceService.edit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Edit resource successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Edit resource fail.");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelRes(@RequestBody @Validated(value = ResForm.KeyCheck.class) ResForm form) {
        int status = resourceService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Deleted resource successfully");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted resource fail.");
    }

    /**
     * Gets all permission types.
     */
    @PostMapping("/allMask")
    public ApiResult<List<Pair<Integer, String>>> getAllMask() {
        return ApiResult.ofData(MaskUtils.allMask());
    }

    /**
     * Gets the specified role resource permission
     */
    @GetMapping("/get/{roleId}")
    public ApiResult<Map<Long, List<Integer>>> getResByRoleId(@PathVariable(name = "roleId") Long roleId) {
        return ApiResult.ofData(resourceService.getByRoleId(roleId));
    }

}

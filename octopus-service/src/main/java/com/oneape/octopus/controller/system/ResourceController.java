package com.oneape.octopus.controller.system;

import com.oneape.octopus.common.MaskUtils;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.system.form.ResForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.ResourceVO;
import com.oneape.octopus.model.VO.TreeNodeVO;
import com.oneape.octopus.service.system.ResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 资源管理
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
            return ApiResult.ofData("添加资源成功");
        }
        return ApiResult.ofError(StateCode.BizError, "添加资源失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditRes(@RequestBody @Validated(value = ResForm.EditCheck.class) ResForm form) {
        int status = resourceService.edit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("修改资源成功");
        }
        return ApiResult.ofError(StateCode.BizError, "修改资源失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelRes(@RequestBody @Validated(value = ResForm.KeyCheck.class) ResForm form) {
        int status = resourceService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("删除资源成功");
        }
        return ApiResult.ofError(StateCode.BizError, "删除资源失败");
    }

    /**
     * 获取资源树
     */
    @PostMapping("/list")
    public ApiResult<List<ResourceVO>> doList(@RequestBody @Validated ResForm form) {
        List<ResourceVO> vos = resourceService.findTree(form.toDO());
        return ApiResult.ofData(vos);
    }

    /**
     * 获取所有权限种类
     */
    @PostMapping("/allMask")
    public ApiResult<List<Pair<Integer, String>>> getAllMask() {
        return ApiResult.ofData(MaskUtils.allMask());
    }

    /**
     * 获取整个资源树
     */
    @PostMapping("/tree")
    public ApiResult<List<TreeNodeVO>> getFullTree() {
        return ApiResult.ofData(resourceService.fullTree());
    }


    /**
     * 获取指定角色资源权限
     */
    @PostMapping("/get/{roleId}")
    public ApiResult<Map<Long, List<Integer>>> getResByRoleId(@PathVariable(value = "roleId") Long roleId) {
        return ApiResult.ofData(resourceService.getByRoleId(roleId));
    }

}

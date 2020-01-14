package com.oneape.octopus.controller.system;

import com.oneape.octopus.common.MaskUtils;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.system.form.ResForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.ResourceVO;
import com.oneape.octopus.service.ResourceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
        return ApiResult.ofData(status > 0 ? "添加资源成功" : "添加资源失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditRes(@RequestBody @Validated(value = ResForm.EditCheck.class) ResForm form) {
        int status = resourceService.edit(form.toDO());
        return ApiResult.ofData(status > 0 ? "修改资源成功" : "修改资源失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelRes(@RequestBody @Validated(value = ResForm.KeyCheck.class) ResForm form) {
        int status = resourceService.deleteById(form.toDO());
        return ApiResult.ofData(status > 0 ? "删除资源成功" : "删除资源失败");
    }

    @PostMapping("/list")
    public ApiResult<List<ResourceVO>> doList(@RequestBody @Validated ResForm form) {
        List<ResourceVO> vos = resourceService.findTree(form.toDO());
        return ApiResult.ofData(vos);
    }

    /**
     * 获取所有权限种类
     *
     * @return List
     */
    @RequestMapping("/allMask")
    public ApiResult<List<Pair<Integer, String>>> getAllMask() {
        return ApiResult.ofData(MaskUtils.allMask());
    }

}

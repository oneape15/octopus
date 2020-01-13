package com.oneape.octopus.controller.peekdata;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.controller.peekdata.form.TagForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.ModelTagVO;
import com.oneape.octopus.service.ModelTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/peek/tag")
public class ModelTagController {

    @Autowired
    private ModelTagService modelTagService;

    @PostMapping("/add")
    public ApiResult<String> doAddTag(@RequestBody @Validated(value = TagForm.AddCheck.class) TagForm form) {
        int status = modelTagService.insert(form.toDO());
        return ApiResult.ofData(status > 0 ? "添加标签成功" : "添加标签失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditTag(@RequestBody @Validated(value = TagForm.EditCheck.class) TagForm form) {
        int status = modelTagService.edit(form.toDO());
        return ApiResult.ofData(status > 0 ? "修改标签成功" : "修改标签失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelTag(@RequestBody @Validated(value = TagForm.KeyCheck.class) TagForm form) {
        int status = modelTagService.deleteById(form.toDO());
        return ApiResult.ofData(status > 0 ? "删除标签成功" : "删除标签失败");
    }

    @PostMapping("/list")
    public ApiResult<PageInfo<ModelTagVO>> doList(@RequestBody @Validated TagForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<ModelTagVO> vos = modelTagService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }
}

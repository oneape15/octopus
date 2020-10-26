package com.oneape.octopus.controller.peekdata;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.GlobalConstant;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.peekdata.form.PeekForm;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.model.domain.peekdata.PeekDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.service.peekdata.PeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/peek")
public class PeekController {

    @Resource
    private PeekService peekService;

    @PostMapping("/add")
    public ApiResult<String> doAddPeek(@RequestBody @Validated(value = PeekForm.AddCheck.class) PeekForm form) {
        int status = peekService.savePeekInfo(form.toDO(), form.getFields(), form.getRules());
        if (status == GlobalConstant.SUCCESS) {
            return ApiResult.ofData("添加取数实例成功");
        } else {
            return ApiResult.ofError(StateCode.BizError, "添加取数实例失败");
        }
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditPeek(@RequestBody @Validated(value = PeekForm.EditCheck.class) PeekForm form) {
        int status = peekService.savePeekInfo(form.toDO(), form.getFields(), form.getRules());
        if (status == GlobalConstant.SUCCESS) {
            return ApiResult.ofData("修改取数实例成功");
        } else {
            return ApiResult.ofError(StateCode.BizError, "修改取数实例失败");
        }
    }

    @PostMapping("/del")
    public ApiResult<String> doDelPeek(@RequestBody @Validated(value = PeekForm.KeyCheck.class) PeekForm form) {
        int status = peekService.deleteById(form.getPeekId());
        if (status == GlobalConstant.SUCCESS) {
            return ApiResult.ofData("删除取数实例成功");
        } else {
            return ApiResult.ofError(StateCode.BizError, "删除取数实例失败");
        }
    }

    @PostMapping("/list")
    public ApiResult<PageInfo<PeekDO>> doList(@RequestBody @Validated PeekForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<PeekDO> vos = peekService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 数据预览
     */
    @PostMapping("/previewData")
    public ApiResult<Result> previewData(@RequestBody @Validated(value = PeekForm.PreviewCheck.class) PeekForm form) {
        return ApiResult.ofData(peekService.previewData(form.getModelId(), form.getFields(), form.getRules()));
    }

    /**
     * 获取数据类型对应的取数规则
     */
    @PostMapping("/dataTypeRules")
    public ApiResult<Map<String, List<Pair<String, String>>>> getAllDataTypeRule() {
        return ApiResult.ofData(peekService.ruleOfDataTypes());
    }

    /**
     * 进行取数操作
     */
    @PostMapping("/peekData")
    public ApiResult<String> peekData(@RequestBody @Validated(value = PeekForm.KeyCheck.class) PeekForm form) {
        int status = peekService.peekData(form.getPeekId());
        if (status == GlobalConstant.SUCCESS) {
            return ApiResult.ofData("取数成功");
        } else {
            return ApiResult.ofError(StateCode.BizError, "取数失败");
        }
    }

    /**
     * 获取取数详细信息
     */
    @PostMapping("/getById")
    public ApiResult<PeekDO> getById(@RequestBody @Validated(value = PeekForm.KeyCheck.class) PeekForm form) {
        return ApiResult.ofData(peekService.findById(form.getPeekId()));
    }
}

package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.controller.system.form.CommonInfoForm;
import com.oneape.octopus.model.DO.system.CommonInfoDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.CommonInfoVO;
import com.oneape.octopus.service.system.CommonInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/commInfo")
public class CommonInfoController {
    @Resource
    private CommonInfoService commonInfoService;

    @PostMapping("/add")
    public ApiResult<String> doAddCommonInfo(@RequestBody @Validated(value = CommonInfoForm.AddCheck.class) CommonInfoForm form) {
        int status = commonInfoService.insert(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Added base information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Added base information fail.");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditCommonInfo(@RequestBody @Validated(value = CommonInfoForm.EditCheck.class) CommonInfoForm form) {
        int status = commonInfoService.edit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Edit base information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Edit base information fail.");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelCommonInfo(@RequestBody @Validated(value = CommonInfoForm.KeyCheck.class) CommonInfoForm form) {
        int status = commonInfoService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Deleted base information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted base information fail.");
    }

    @PostMapping("/list")
    public ApiResult<PageInfo<CommonInfoDO>> doList(@RequestBody @Validated CommonInfoForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<CommonInfoDO> list = commonInfoService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(list));
    }

    @GetMapping("/AllClassify")
    public ApiResult<List<String>> getAllClassify() {
        return ApiResult.ofData(commonInfoService.getAllClassify());
    }
}

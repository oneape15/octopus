package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.controller.system.form.CommonInfoForm;
import com.oneape.octopus.model.domain.system.CommonInfoDO;
import com.oneape.octopus.model.VO.ApiResult;
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

    @PostMapping("/save")
    public ApiResult<String> doSaveCommonInfo(@RequestBody @Validated(value = CommonInfoForm.InfoCheck.class) CommonInfoForm form) {
        int status = commonInfoService.save(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Save base information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Save base information fail.");
    }

    @GetMapping("/del/{id}")
    public ApiResult<String> doDelCommonInfo(@PathVariable(name = "id") Long id) {
        int status = commonInfoService.deleteById(id);
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

    @GetMapping("/allClassify")
    public ApiResult<List<String>> getAllClassify() {
        return ApiResult.ofData(commonInfoService.getAllClassify());
    }

    /**
     * Get the basic information list according to classify
     */
    @PostMapping("/getInfo")
    public ApiResult getInfoByClassify(@RequestBody @Validated CommonInfoForm form) {
        List<CommonInfoDO> list = commonInfoService.find(form.toDO());
        return ApiResult.ofData(list);
    }
}

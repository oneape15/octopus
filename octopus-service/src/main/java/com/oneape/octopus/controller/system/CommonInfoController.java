package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.controller.system.form.CommonInfoForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.CommonInfoVO;
import com.oneape.octopus.service.system.CommonInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 基础信息
 */
@RestController
@RequestMapping("/commInfo")
public class CommonInfoController {
    @Resource
    private CommonInfoService commonInfoService;

    @PostMapping("/add")
    public ApiResult<String> doAddCommonInfo(@RequestBody @Validated(value = CommonInfoForm.AddCheck.class) CommonInfoForm form) {
        int status = commonInfoService.insert(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("添加基础信息成功");
        }
        return ApiResult.ofError(StateCode.BizError, "添加基础信息失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditCommonInfo(@RequestBody @Validated(value = CommonInfoForm.EditCheck.class) CommonInfoForm form) {
        int status = commonInfoService.edit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("修改基础信息成功");
        }
        return ApiResult.ofError(StateCode.BizError, "修改基础信息失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelCommonInfo(@RequestBody @Validated(value = CommonInfoForm.KeyCheck.class) CommonInfoForm form) {
        int status = commonInfoService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("删除基础信息成功");
        }
        return ApiResult.ofError(StateCode.BizError, "删除基础信息失败");
    }

    @PostMapping("/list")
    public ApiResult<PageInfo<CommonInfoVO>> doList(@RequestBody @Validated CommonInfoForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<CommonInfoVO> vos = commonInfoService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    @PostMapping("/getClassify")
    public ApiResult<List<String>> getClassify(@RequestBody CommonInfoForm form) {
        String classify = form.getClassify();
        return ApiResult.ofData(commonInfoService.getAllClassify());
    }
}

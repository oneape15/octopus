package com.oneape.octopus.controller.peekdata;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.controller.peekdata.form.PeekForm;
import com.oneape.octopus.datasource.PeekRuleTypeHelper;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.PeekVO;
import com.oneape.octopus.service.PeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/peek")
public class PeekController {

    @Autowired
    private PeekService peekService;

    @PostMapping("/add")
    public ApiResult<String> doAddPeek(@RequestBody @Validated(value = PeekForm.AddCheck.class) PeekForm form) {
        int status = peekService.insert(form.toDO());
        return ApiResult.ofData(status > 0 ? "添加标签成功" : "添加标签失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditPeek(@RequestBody @Validated(value = PeekForm.EditCheck.class) PeekForm form) {
        int status = peekService.edit(form.toDO());
        return ApiResult.ofData(status > 0 ? "修改标签成功" : "修改标签失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelPeek(@RequestBody @Validated(value = PeekForm.KeyCheck.class) PeekForm form) {
        int status = peekService.deleteById(form.toDO());
        return ApiResult.ofData(status > 0 ? "删除标签成功" : "删除标签失败");
    }

    @PostMapping("/list")
    public ApiResult<PageInfo<PeekVO>> doList(@RequestBody @Validated PeekForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<PeekVO> vos = peekService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 获取数据类型对应的取数规则
     *
     * @return List
     */
    @PostMapping("/dataTypeRules")
    public ApiResult<Map<String, List<PeekRuleTypeHelper>>> getAllDataTypeRule() {
        return ApiResult.ofData(peekService.ruleOfDataTypes());
    }
}

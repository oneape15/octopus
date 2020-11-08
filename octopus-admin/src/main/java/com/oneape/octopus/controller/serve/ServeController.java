package com.oneape.octopus.controller.serve;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.serve.form.ServeForm;
import com.oneape.octopus.domain.serve.ServeInfoDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.commons.enums.ReportParamType;
import com.oneape.octopus.commons.enums.ServeType;
import com.oneape.octopus.commons.enums.VisualType;
import com.oneape.octopus.service.serve.ServeInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/serve")
public class ServeController {

    @Resource
    private ServeInfoService reportService;

    /**
     * Save serve information.
     */
    @PostMapping("/design/save")
    public ApiResult<String> doSaveReport(@RequestBody @Validated(value = ServeForm.AddCheck.class) ServeForm form) {
        ServeInfoDO infoDO = form.toDO();
        int status = reportService.save(infoDO);
        if (status > 0) {
            return ApiResult.ofData("Saved serve information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Saved serve information fail.");
    }

    /**
     * Deleted serve information.
     */
    @PostMapping("/design/del")
    public ApiResult<String> doDelReport(@RequestBody @Validated(value = ServeForm.KeyCheck.class) ServeForm form) {
        int status = reportService.deleteById(form.getId());
        if (status > 0) {
            return ApiResult.ofData("Deleted serve information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted serve information fail.");
    }

    /**
     * Paging query serve information.
     */
    @PostMapping("/design/list")
    public ApiResult<PageInfo<ServeInfoDO>> doList(@RequestBody @Validated ServeForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<ServeInfoDO> vos = reportService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }


    /**
     * Paging query serve information.
     */
    @GetMapping("/design/get/{id}")
    public ApiResult<ServeInfoDO> getById(@PathVariable(name = "id") Long id) {
        ServeInfoDO vo = reportService.findById(id);
        return ApiResult.ofData(vo);
    }

    /**
     * Get serve type.
     */
    @GetMapping("/design/types")
    public ApiResult<List<Pair<String, String>>> getReportTypes() {
        List<Pair<String, String>> types = new ArrayList<>();
        for (ServeType rt : ServeType.values()) {
            types.add(new Pair<>(rt.getCode(), rt.getDesc()));
        }
        return ApiResult.ofData(types);
    }


    /**
     * Get serve visual type.
     */
    @GetMapping("/design/visualTypes")
    public ApiResult<List<Pair<Integer, String>>> getReportVisualTypes() {
        List<Pair<Integer, String>> types = new ArrayList<>();
        for (VisualType rt : VisualType.values()) {
            types.add(new Pair<>(rt.getCode(), rt.getDesc()));
        }
        return ApiResult.ofData(types);
    }

    /**
     * Get serve param type.
     */
    @GetMapping("/design/paramTypes")
    public ApiResult<List<Pair<Integer, String>>> getReportParamTypes() {
        List<Pair<Integer, String>> pairs = new ArrayList<>();
        for (ReportParamType ppt : ReportParamType.values()) {
            pairs.add(new Pair<>(ppt.getCode(), ppt.getDesc()));
        }
        return ApiResult.ofData(pairs);
    }
}
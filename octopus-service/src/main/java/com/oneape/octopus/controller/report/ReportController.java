package com.oneape.octopus.controller.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.report.form.ReportForm;
import com.oneape.octopus.controller.report.form.ReportSqlForm;
import com.oneape.octopus.model.DO.report.ReportColumnDO;
import com.oneape.octopus.model.DO.report.ReportDO;
import com.oneape.octopus.model.DO.report.ReportDslDO;
import com.oneape.octopus.model.DO.report.ReportParamDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.enums.ReportParamType;
import com.oneape.octopus.model.enums.ReportType;
import com.oneape.octopus.service.report.ReportService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    /**
     * Save report information.
     */
    @PostMapping("/design/save")
    public ApiResult<String> doSaveReport(@RequestBody @Validated(value = ReportForm.AddCheck.class) ReportForm form) {
        int status = reportService.saveReportInfo(form.toDTO());
        if (status > 0) {
            return ApiResult.ofData("Saved report information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Saved report information fail.");
    }

    /**
     * Deleted report information.
     */
    @PostMapping("/design/del")
    public ApiResult<String> doDelReport(@RequestBody @Validated(value = ReportForm.KeyCheck.class) ReportForm form) {
        int status = reportService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Deleted report information successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Deleted report information fail.");
    }

    /**
     * Paging query report information.
     */
    @PostMapping("/design/list")
    public ApiResult<PageInfo<ReportDO>> doList(@RequestBody @Validated ReportForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<ReportDO> vos = reportService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * Get report type.
     */
    @GetMapping("/design/types")
    public ApiResult<List<Pair<String, String>>> getReportTypes() {
        List<Pair<String, String>> types = new ArrayList<>();
        for (ReportType rt : ReportType.values()) {
            types.add(new Pair<>(rt.getCode(), rt.getDesc()));
        }
        return ApiResult.ofData(types);
    }

    /**
     * Get report param type.
     */
    @GetMapping("/design/paramTypes")
    public ApiResult<List<Pair<Integer, String>>> getReportParamTypes() {
        List<Pair<Integer, String>> pairs = new ArrayList<>();
        for (ReportParamType ppt : ReportParamType.values()) {
            pairs.add(new Pair<>(ppt.getCode(), ppt.getDesc()));
        }
        return ApiResult.ofData(pairs);
    }


    /**
     * Get report param information base on reportId.
     */
    @GetMapping("/design/params/{reportId}")
    public ApiResult<List<ReportParamDO>> getParamByReportId(@PathVariable(name = "reportId") Long reportId) {
        return ApiResult.ofData(reportService.getParamByReportId(reportId));
    }

    /**
     * Report query parameters are saved.
     */
    @PostMapping("/design/saveParams")
    public ApiResult<String> saveParams(@RequestBody @Validated(value = ReportForm.KeyCheck.class) ReportForm form) {
        int status = reportService.saveReportParams(form.getReportId(), form.getParams());
        if (status > 0) {
            return ApiResult.ofData("Report query parameters are saved successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Report query parameters are saved fail.");
    }

    /**
     * Get report columns information base on reportId.
     */
    @PostMapping("/design/columns/{reportId}")
    public ApiResult<List<ReportColumnDO>> getColumnByReportId(@PathVariable(name = "reportId") Long reportId) {
        return ApiResult.ofData(reportService.getColumnByReportId(reportId));
    }

    /**
     * Report query columns saved.
     */
    @PostMapping("/design/saveColumns")
    public ApiResult<String> saveColumns(@RequestBody @Validated(value = ReportForm.KeyCheck.class) ReportForm form) {
        int status = reportService.saveReportColumns(form.getReportId(), form.getColumns());
        if (status > 0) {
            return ApiResult.ofData("Report query columns saved successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Report query columns saved fail.");
    }

    /**
     * Report dsl sql saved.
     */
    @PostMapping("/design/saveSql")
    public ApiResult<String> saveSql(@RequestBody @Validated(value = ReportSqlForm.SaveCheck.class) ReportSqlForm form) {
        int status = reportService.saveReportDslSql(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("Report dsl sql saved successfully.");
        }
        return ApiResult.ofError(StateCode.BizError, "Report dsl sql saved fail.");
    }

    /**
     * Get the sql dsl information base on sqlId.
     */
    @GetMapping("/design/sql/{sqlId}")
    public ApiResult<ReportDslDO> getReportSql(@PathVariable(name = "sqlId") Long sqlId) {
        return ApiResult.ofData(reportService.getReportSql(sqlId));
    }
}
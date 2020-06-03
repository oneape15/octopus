package com.oneape.octopus.controller.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.common.enums.ReportParamType;
import com.oneape.octopus.common.enums.ReportType;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.report.form.ReportForm;
import com.oneape.octopus.controller.report.form.ReportSqlForm;
import com.oneape.octopus.model.VO.*;
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
     * 添加报表
     */
    @PostMapping("/design/add")
    public ApiResult<String> doAddReport(@RequestBody @Validated(value = ReportForm.AddCheck.class) ReportForm form) {
        int status = reportService.addReportInfo(form.toVO());
        if (status > 0) {
            return ApiResult.ofData("创建报表成功");
        }
        return ApiResult.ofError(StateCode.BizError, "创建报表失败");
    }

    /**
     * 修改报表
     */
    @PostMapping("/design/edit")
    public ApiResult<String> doEditReport(@RequestBody @Validated(value = ReportForm.EditCheck.class) ReportForm form) {
        int status = reportService.editReportInfo(form.toVO());
        if (status > 0) {
            return ApiResult.ofData("修改报表成功");
        }
        return ApiResult.ofError(StateCode.BizError, "修改报表失败");
    }

    /**
     * 删除报表
     */
    @PostMapping("/design/del")
    public ApiResult<String> doDelReport(@RequestBody @Validated(value = ReportForm.KeyCheck.class) ReportForm form) {
        int status = reportService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("删除报表成功");
        }
        return ApiResult.ofError(StateCode.BizError, "删除报表失败");
    }

    /**
     * 复制报表
     */
    @PostMapping(value = "/design/copy/{reportId}", consumes = "application/json")
    public ApiResult<String> doCopy(@PathVariable(name = "reportId") Long reportId) {
        int status = reportService.copyReport(reportId);
        if (status > 0) {
            return ApiResult.ofData("复制报表成功");
        }
        return ApiResult.ofError(StateCode.BizError, "复制报表失败");
    }

    /**
     * 分页查询
     */
    @PostMapping("/design/list")
    public ApiResult<PageInfo<ReportVO>> doList(@RequestBody @Validated ReportForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<ReportVO> vos = reportService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 获取报表类型
     */
    @PostMapping("/design/types")
    public ApiResult<List<Pair<String, String>>> getReportTypes() {
        List<Pair<String, String>> types = new ArrayList<>();
        for (ReportType rt : ReportType.values()) {
            types.add(new Pair<>(rt.getCode(), rt.getDesc()));
        }
        return ApiResult.ofData(types);
    }

    /**
     * 获取查询字段类型
     */
    @PostMapping("/design/paramTypes")
    public ApiResult<List<Pair<Integer, String>>> getReportParamTypes() {
        List<Pair<Integer, String>> pairs = new ArrayList<>();
        for (ReportParamType ppt : ReportParamType.values()) {
            pairs.add(new Pair<>(ppt.getCode(), ppt.getDesc()));
        }
        return ApiResult.ofData(pairs);
    }


    /**
     * 获取报表查询参数信息
     */
    @PostMapping("/design/params/{reportId}")
    public ApiResult<List<ReportParamVO>> getParamByReportId(@PathVariable(name = "reportId") Long reportId) {
        return ApiResult.ofData(reportService.getParamByReportId(reportId));
    }

    /**
     * 保存查询参数信息
     */
    @PostMapping("/design/saveParams")
    public ApiResult<String> saveParams(@RequestBody @Validated(value = ReportForm.KeyCheck.class) ReportForm form) {
        int status = reportService.saveReportParams(form.getReportId(), form.getReportParams());
        if (status > 0) {
            return ApiResult.ofData("报表查询参数保存成功");
        }
        return ApiResult.ofError(StateCode.BizError, "报表查询参数保存失败");
    }

    /**
     * 获取报表字段信息
     */
    @PostMapping("/design/columns/{reportId}")
    public ApiResult<List<ReportColumnVO>> getColumnByReportId(@PathVariable(name = "reportId") Long reportId) {
        return ApiResult.ofData(reportService.getColumnByReportId(reportId));
    }

    /**
     * 保存报表字段信息
     */
    @PostMapping("/design/saveColumns")
    public ApiResult<String> saveColumns(@RequestBody @Validated(value = ReportForm.KeyCheck.class) ReportForm form) {
        int status = reportService.saveReportColumns(form.getReportId(), form.getReportColumns());
        if (status > 0) {
            return ApiResult.ofData("报表字段保存成功");
        }
        return ApiResult.ofError(StateCode.BizError, "报表字段保存失败");
    }

    /**
     * sql信息保存
     */
    @PostMapping("/design/saveSql")
    public ApiResult<String> saveSql(@RequestBody @Validated(value = ReportSqlForm.SaveCheck.class) ReportSqlForm form) {
        int status = reportService.saveReportSql(form.getReportId(), form.toDO());
        if (status > 0) {
            return ApiResult.ofData("报表SQL保存成功");
        }
        return ApiResult.ofError(StateCode.BizError, "报表SQL保存失败");
    }

    /**
     * 根据SQL主键获取详细信息
     */
    @PostMapping("/design/sql/{sqlId}")
    public ApiResult<ReportSqlVO> getReportSql(@PathVariable(name = "sqlId") Long sqlId) {
        return ApiResult.ofData(reportService.getReportSql(sqlId));
    }
}
package com.oneape.octopus.controller.report;

import com.oneape.octopus.controller.report.form.ReportForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping("/add")
    public ApiResult doAddReport(@RequestBody @Validated(value = ReportForm.AddCheck.class) ReportForm form) {
        return ApiResult.ofData(null);
    }

    @RequestMapping("/edit")
    public ApiResult doEditReport(@RequestBody @Validated(value = ReportForm.EditCheck.class) ReportForm form) {
        return ApiResult.ofData(null);
    }

    @RequestMapping("/del")
    public ApiResult doDelReport(@RequestBody @Validated(value = ReportForm.KeyCheck.class) ReportForm form) {
        return ApiResult.ofData(null);
    }
}

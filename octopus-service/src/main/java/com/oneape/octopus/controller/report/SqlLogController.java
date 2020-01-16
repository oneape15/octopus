package com.oneape.octopus.controller.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.controller.report.form.SqlLogForm;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.SqlLogVO;
import com.oneape.octopus.service.SqlLogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/report/log")
public class SqlLogController {

    @Resource
    private SqlLogService sqlLogService;

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<SqlLogVO>> doList(@RequestBody @Validated SqlLogForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<SqlLogVO> vos = sqlLogService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 根据Id获取详细
     */
    @PostMapping("/detail/{logId}")
    public ApiResult<SqlLogVO> getDetail(@PathVariable("logId") Long logId) {
        return ApiResult.ofData(sqlLogService.getById(logId));
    }
}

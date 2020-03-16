package com.oneape.octopus.controller.query;


import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.ReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询接口功能
 * Created by oneape<oneape15@163.com>
 * Created 2020-03-02 11:33.
 * Modify:
 */

@Slf4j
@RestController
@RequestMapping("/query")
public class DataQueryController {

    @PostMapping("/fetchReportInfo/{code}")
    public ApiResult<ReportVO> fetchReportInfo(@PathVariable("code") String code) {
        log.debug(code);
        return ApiResult.ofData(null);
    }

}

package com.oneape.octopus.controller.peekdata;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.controller.peekdata.form.DataImportForm;
import com.oneape.octopus.datasource.data.Result;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.ImportRecordVO;
import com.oneape.octopus.service.ImportRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据导入管理
 */
@Slf4j
@RestController
@RequestMapping("/peek/import")
public class DataImportController {

    @Resource
    private ImportRecordService importRecordService;

    /**
     * 数据导入
     */
    @RequestMapping("/upload")
    public ApiResult<String> uploadData(@RequestBody @Validated(value = DataImportForm.ImportCheck.class) DataImportForm form,
                                        @RequestParam("file") MultipartFile file) {
        int status = importRecordService.uploadData(form.toDO(), file);
        if (status > 0) {
            return ApiResult.ofData("数据导入成功");
        }
        return ApiResult.ofError(StateCode.BizError, "导入数据失败");
    }

    /**
     * 查询导入数据
     */
    @PostMapping("/list")
    public ApiResult<PageInfo<ImportRecordVO>> queryRecordList(@RequestBody DataImportForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<ImportRecordVO> records = importRecordService.list(form.toDO());
        return ApiResult.ofData(new PageInfo<>(records));
    }

    /**
     * 预览数据
     */
    @PostMapping("/preview")
    public ApiResult<Result> previewImportData(@RequestBody DataImportForm form) {
        Result ret = importRecordService.previewData(form.toDO());
        return ApiResult.ofData(ret);
    }

    /**
     * 删除导入数据
     */
    @PostMapping("/del")
    public ApiResult<String> delImportData(@RequestBody DataImportForm form) {
        try {
            importRecordService.archiveData(form.toDO());
        } catch (Exception e) {
            return ApiResult.ofError(StateCode.BizError, e.getMessage());
        }
        return ApiResult.ofData("删除数据成功");
    }
}

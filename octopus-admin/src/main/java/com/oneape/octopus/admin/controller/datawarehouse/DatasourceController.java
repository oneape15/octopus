package com.oneape.octopus.admin.controller.datawarehouse;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.admin.controller.datawarehouse.form.DatasourceForm;
import com.oneape.octopus.commons.dto.DataType;
import com.oneape.octopus.datasource.DatasourceFactory;
import com.oneape.octopus.datasource.data.DatasourceInfo;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.domain.schema.DatasourceDO;
import com.oneape.octopus.admin.model.vo.ApiResult;
import com.oneape.octopus.admin.service.schema.DatasourceService;
import com.oneape.octopus.admin.service.schema.SchemaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data source information management。
 */
@RestController
@RequestMapping("/ds")
public class DatasourceController {
    @Resource
    private DatasourceService datasourceService;
    @Resource
    private SchemaService     schemaService;
    @Resource
    private DatasourceFactory datasourceFactory;

    @PostMapping("/save")
    public ApiResult<String> doSaveDatasource(@RequestBody @Validated(value = DatasourceForm.AddCheck.class) DatasourceForm form) {
        ApiResult<String> ret = testConnection(form);
        if (ret.isSuccess()) {
            int status = datasourceService.save(form.toDO());
            if (status > 0) {
                return ApiResult.ofData(I18nMsgConfig.getMessage("ds.save.success"));
            }
            return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ds.save.fail"));
        }
        return ret;
    }

    @PostMapping("/del/{id}")
    public ApiResult<String> doDelDs(@PathVariable(name = "id") Long id) {
        int status = datasourceService.deleteById(id);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ds.del.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ds.del.fail"));
    }

    @PostMapping("/changeStatus")
    public ApiResult<String> doChangeStatus(@RequestBody @Validated(value = DatasourceForm.KeyCheck.class) DatasourceForm form) {
        boolean status = datasourceService.changeStatus(form.getId(), form.getStatus());
        if (status) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ds.changeStatus.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ds.changeStatus.fail"));
    }

    @PostMapping("/syncSchema")
    public ApiResult<String> syncSchema(@RequestBody @Validated(value = DatasourceForm.KeyCheck.class) DatasourceForm form) {
        Preconditions.checkNotNull(datasourceService.findById(form.getId()), I18nMsgConfig.getMessage("ds.id.invalid"));
        int status = schemaService.fetchAndSaveDatabaseInfo(form.getId());
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ds.sync.success"));
        }

        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ds.sync.fail"));
    }

    @PostMapping(value = "/list")
    public ApiResult<PageInfo<DatasourceDO>> list(@RequestBody @Validated DatasourceForm form) {
        PageHelper.startPage(form.getCurrent(), form.getPageSize());
        List<DatasourceDO> vos = datasourceService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * Gets the supported data source type
     */
    @GetMapping(value = "/getAllDsTypes")
    public ApiResult<List<Map<String, String>>> getAllDsTypes() {
        List<Map<String, String>> list = new ArrayList<>();
        for (DatasourceTypeHelper dt : DatasourceTypeHelper.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("type", dt.name());
            map.put("driver", dt.getDriverClass());
            list.add(map);
        }
        return ApiResult.ofData(list);
    }


    /**
     * Take all available data sources.
     */
    @GetMapping(value = "/getAllSimple")
    public ApiResult<List<DatasourceDO>> getAllSimple() {
        return ApiResult.ofData(datasourceService.find(new DatasourceDO()));
    }

    /**
     * Test whether the data source is available.
     */
    @PostMapping(value = "/test")
    public ApiResult<String> testConnection(@RequestBody @Validated(value = DatasourceForm.AddCheck.class) DatasourceForm form) {
        DatasourceInfo dsi = new DatasourceInfo();
        dsi.setUrl(form.getJdbcUrl());
        dsi.setUsername(form.getUsername());
        dsi.setPassword(form.getPassword());
        dsi.setDatasourceType(DatasourceTypeHelper.byName(form.getType()));
        dsi.setTestSql(StringUtils.isBlank(form.getTestSql()) ? "select 1" : form.getTestSql());
        boolean success = datasourceFactory.testDatasource(dsi);
        if (success) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ds.test.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ds.test.fail"));
    }


    /**
     * Gets all data types.
     */
    @GetMapping("/getDataTypes")
    public ApiResult<List<String>> getDataTypes() {
        List<String> ret = new ArrayList<>();
        for (DataType dt : DataType.values()) {
            ret.add(dt.name());
        }
        return ApiResult.ofData(ret);
    }

}

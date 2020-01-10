package com.oneape.octopus.controller.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oneape.octopus.controller.report.form.DatasourceForm;
import com.oneape.octopus.datasource.DatasourceFactory;
import com.oneape.octopus.datasource.DatasourceInfo;
import com.oneape.octopus.datasource.DatasourceTypeHelper;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.DatasourceVO;
import com.oneape.octopus.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ds")
public class DatasourceController {
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private DatasourceFactory datasourceFactory;

    @PostMapping("/add")
    public ApiResult<String> doAddDs(@RequestBody @Validated(value = DatasourceForm.AddCheck.class) DatasourceForm form) {
        int status = datasourceService.insert(form.toDO());
        return ApiResult.ofData(status > 0 ? "添加数据源成功" : "添加数据源失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditDs(@RequestBody @Validated(value = DatasourceForm.EditCheck.class) DatasourceForm form) {
        int status = datasourceService.edit(form.toDO());
        return ApiResult.ofData(status > 0 ? "修改数据源成功" : "修改数据源失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelDs(@RequestBody @Validated(value = DatasourceForm.KeyCheck.class) DatasourceForm form) {
        int status = datasourceService.deleteById(form.toDO());
        return ApiResult.ofData(status > 0 ? "删除数据源成功" : "删除数据源失败");
    }

    @PostMapping("/list")
    public ApiResult<PageInfo<DatasourceVO>> list(@RequestBody @Validated DatasourceForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<DatasourceVO> vos = datasourceService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 获取支持的数据源类型
     *
     * @return ApiResult
     */
    @RequestMapping("/getAllDsTypes")
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
     * 数据源测试
     *
     * @param form DatasourceForm
     */
    @RequestMapping("/test")
    public ApiResult<String> testConnection(@RequestBody @Validated(value = DatasourceForm.AddCheck.class) DatasourceForm form) {
        DatasourceInfo dsi = new DatasourceInfo();
        dsi.setUrl(form.getJdbcUrl());
        dsi.setUsername(form.getUsername());
        dsi.setPassword(form.getPassword());
        dsi.setDatasourceType(DatasourceTypeHelper.byName(form.getType()));
        dsi.setTestSql("select 1");
        boolean success = datasourceFactory.testDatasource(dsi);
        if (success) {
            return ApiResult.ofData("数据库连接测试成功");
        } else {
            return ApiResult.ofError(1000, "数据库连接测试失败");
        }
    }

}

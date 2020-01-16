package com.oneape.octopus.controller.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.oneape.octopus.common.StateCode;
import com.oneape.octopus.commons.value.Pair;
import com.oneape.octopus.controller.system.form.DatasourceForm;
import com.oneape.octopus.datasource.*;
import com.oneape.octopus.datasource.schema.FieldInfo;
import com.oneape.octopus.datasource.schema.TableInfo;
import com.oneape.octopus.model.DO.report.DatasourceDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.model.VO.DatasourceVO;
import com.oneape.octopus.service.DatasourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 数据源管理
 */
@RestController
@RequestMapping("/ds")
public class DatasourceController {
    @Resource
    private DatasourceService datasourceService;
    @Resource
    private DatasourceFactory datasourceFactory;
    @Resource
    private QueryFactory queryFactory;

    @PostMapping("/add")
    public ApiResult<String> doAddDs(@RequestBody @Validated(value = DatasourceForm.AddCheck.class) DatasourceForm form) {
        int status = datasourceService.insert(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("添加数据源成功");
        }
        return ApiResult.ofError(StateCode.BizError, "添加数据源失败");
    }

    @PostMapping("/edit")
    public ApiResult<String> doEditDs(@RequestBody @Validated(value = DatasourceForm.EditCheck.class) DatasourceForm form) {
        int status = datasourceService.edit(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("修改数据源成功");
        }
        return ApiResult.ofError(StateCode.BizError, "修改数据源失败");
    }

    @PostMapping("/del")
    public ApiResult<String> doDelDs(@RequestBody @Validated(value = DatasourceForm.KeyCheck.class) DatasourceForm form) {
        int status = datasourceService.deleteById(form.toDO());
        if (status > 0) {
            return ApiResult.ofData("删除数据源成功");
        }
        return ApiResult.ofError(StateCode.BizError, "删除数据源失败");
    }

    @PostMapping("/list")
    public ApiResult<PageInfo<DatasourceVO>> list(@RequestBody @Validated DatasourceForm form) {
        PageHelper.startPage(form.getCurrentPage(), form.getPageSize());
        List<DatasourceVO> vos = datasourceService.find(form.toDO());
        return ApiResult.ofData(new PageInfo<>(vos));
    }

    /**
     * 获取支持的数据源类型
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
     * 获取所有可用数据源
     */
    @RequestMapping("/getAllSimple")
    public ApiResult<List<DatasourceVO>> getAllSimple() {
        return ApiResult.ofData(datasourceService.find(new DatasourceDO()));
    }

    /**
     * 数据源测试
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
        }
        return ApiResult.ofError(StateCode.BizError, "数据库连接测试失败");
    }

    /**
     * 根据数据源id 获取包括的所有表信息
     */
    @PostMapping("/getTables")
    public ApiResult<List<TableInfo>> getDatasourceTables(@RequestBody @Validated(value = DatasourceForm.KeyCheck.class) DatasourceForm form) {
        DatasourceInfo dsi = Preconditions.checkNotNull(datasourceService.getDatasourceInfoById(form.getDsId()),
                "数据源信息为空");

        String schema = queryFactory.getSchema(dsi);
        List<TableInfo> tables = queryFactory.allTables(dsi, schema);

        return ApiResult.ofData(tables);
    }

    /**
     * 根据数据源id 获取包括的所有表及其字段信息
     */
    @PostMapping("/getTablesAndColumns")
    public ApiResult<List<Pair<String, List<String>>>> getDataSourceDetail(
            @RequestBody @Validated(value = DatasourceForm.KeyCheck.class) DatasourceForm form) {
        List<Pair<String, List<String>>> pairs = new ArrayList<>();
        DatasourceInfo dsi = Preconditions.checkNotNull(datasourceService.getDatasourceInfoById(form.getDsId()),
                "数据源信息为空");

        String schema = queryFactory.getSchema(dsi);
        List<FieldInfo> fields = queryFactory.allFields(dsi, schema);
        if (CollectionUtils.isNotEmpty(fields)) {
            Map<String, List<String>> map = new HashMap<>();
            fields.forEach(f -> {
                if (!map.containsKey(f.getTableName())) {
                    map.put(f.getTableName(), new ArrayList<>());
                }
                map.get(f.getTableName()).add(f.getName());
            });

            map.forEach((key, value) -> pairs.add(new Pair<>(key, value)));
        }
        return ApiResult.ofData(pairs);
    }


    /**
     * 获取所有数据类型
     */
    @PostMapping("/getDataTypes")
    public ApiResult<List<String>> getDataTypes() {
        List<String> ret = new ArrayList<>();
        for (DataType dt : DataType.values()) {
            ret.add(dt.name());
        }
        return ApiResult.ofData(ret);
    }

}

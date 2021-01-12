package com.oneape.octopus.controller.datawarehouse;

import com.oneape.octopus.controller.datawarehouse.form.TableColumnForm;
import com.oneape.octopus.controller.datawarehouse.form.TableSchemaForm;
import com.oneape.octopus.domain.schema.TableColumnDO;
import com.oneape.octopus.domain.schema.TableSchemaDO;
import com.oneape.octopus.model.vo.ApiResult;
import com.oneape.octopus.service.schema.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-05-30 14:19.
 * Modify:
 */
@Slf4j
@RestController
@RequestMapping("/schema")
public class SchemaController {

    @Resource
    private SchemaService schemaService;

    @GetMapping(value = "/reloadDatabase/{dsId}")
    public ApiResult reloadDatabaseById(@PathVariable(name = "dsId") Long dsId) {
        int status = schemaService.fetchAndSaveDatabaseInfo(dsId);
        if (status <= 0) {
            return ApiResult.ofMessage("Pull data source Schema fail!");
        }
        return ApiResult.ofData("Pull data source Schema successfully!");
    }


    @GetMapping(value = "/reload/{dsId}/{tableName}")
    public ApiResult fetchTableInfo(@PathVariable(name = "dsId") Long dsId, @PathVariable(name = "tableName") String tableName) {
        int status = schemaService.fetchAndSaveTableColumnInfo(dsId, tableName);
        if (status <= 0) {
            return ApiResult.ofMessage("Pull table Schema fail!");
        }
        return ApiResult.ofData("Pull table Schema successfully!");
    }

    @GetMapping(value = "/fetchTableList/{dsId}")
    public ApiResult<List<TableSchemaDO>> fetchTableList(@PathVariable(name = "dsId") Long dsId) {
        List<TableSchemaDO> tableSchemaDOs = schemaService.fetchTableList(dsId);

        return ApiResult.ofData(tableSchemaDOs);
    }

    @GetMapping(value = "/fetchTableColumnList/{dsId}/{tableName}")
    public ApiResult<List<TableColumnDO>> fetchTableColumnList(@PathVariable(name = "dsId") Long dsId,
                                                               @PathVariable(name = "tableName") String tableName) {
        List<TableColumnDO> tableSchemaDOs = schemaService.fetchTableColumnList(dsId, tableName);

        return ApiResult.ofData(tableSchemaDOs);
    }

    @PostMapping(value = "/column/changeInfo")
    public ApiResult changeColumnInfo(@RequestBody @Validated(value = TableColumnForm.InfoCheck.class) TableColumnForm form) {
        int status = schemaService.changeTableColumnInfo(form.toDO());
        if (status <= 0) {
            return ApiResult.ofMessage("Modify the table field information fail!");
        }
        return ApiResult.ofData("Modify the table field information successfully!");
    }

    @PostMapping(value = "/table/changeInfo")
    public ApiResult changeTableInfo(@RequestBody @Validated(value = TableSchemaForm.InfoCheck.class) TableSchemaForm form) {
        int status = schemaService.updateTableSchemaInfo(form.toDO());
        if (status <= 0) {
            return ApiResult.ofMessage("Modify the table information fail!");
        }
        return ApiResult.ofData("Modify the table information successfully!");
    }
}
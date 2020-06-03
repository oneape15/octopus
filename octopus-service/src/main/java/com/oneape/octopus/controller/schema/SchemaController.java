package com.oneape.octopus.controller.schema;

import com.oneape.octopus.controller.schema.form.TableColumnForm;
import com.oneape.octopus.model.DO.schema.TableColumnDO;
import com.oneape.octopus.model.DO.schema.TableSchemaDO;
import com.oneape.octopus.model.VO.ApiResult;
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

    @RequestMapping(value = "/reloadDatabase/{dsId}", method = {RequestMethod.GET})
    public ApiResult reloadDatabaseById(@PathVariable(name = "dsId") Long dsId) {
        int status = schemaService.fetchAndSaveDatabaseInfo(dsId);
        if (status <= 0) {
            return ApiResult.ofMessage("Pull data source Schema fail!");
        }
        return ApiResult.ofData("Pull data source Schema successfully!");
    }

    @RequestMapping(value = "/fetchTableList/{dsId}", method = {RequestMethod.GET})
    public ApiResult<List<TableSchemaDO>> fetchTableList(@PathVariable(name = "dsId") Long dsId) {
        List<TableSchemaDO> tableSchemaDOs = schemaService.fetchTableList(dsId);

        return ApiResult.ofData(tableSchemaDOs);
    }

    @RequestMapping(value = "/fetchTableColumnList/{dsId}/{tableName}", method = {RequestMethod.GET})
    public ApiResult<List<TableColumnDO>> fetchTableColumnList(@PathVariable(name = "dsId") Long dsId,
                                                               @PathVariable(name = "tableName") String tableName) {
        List<TableColumnDO> tableSchemaDOs = schemaService.fetchTableColumnList(dsId, tableName);

        return ApiResult.ofData(tableSchemaDOs);
    }

    @RequestMapping(value = "/column/changeInfo", method = {RequestMethod.POST})
    public ApiResult changeColumnInfo(@RequestBody @Validated(value = TableColumnForm.InfoCheck.class) TableColumnForm form) {
        int status = schemaService.changeTableColumnInfo(form.toDO());
        if (status <= 0) {
            return ApiResult.ofMessage("Modify the table field information fail!");
        }
        return ApiResult.ofData("Modify the table field information successfully!");
    }
}

package com.oneape.octopus.admin.controller.datawarehouse;

import com.oneape.octopus.admin.config.I18nMsgConfig;
import com.oneape.octopus.admin.controller.datawarehouse.form.TableColumnForm;
import com.oneape.octopus.admin.controller.datawarehouse.form.TableSchemaForm;
import com.oneape.octopus.admin.service.warehouse.SchemaService;
import com.oneape.octopus.commons.cause.StateCode;
import com.oneape.octopus.commons.dto.ApiResult;
import com.oneape.octopus.domain.warehouse.TableColumnDO;
import com.oneape.octopus.domain.warehouse.TableSchemaDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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

    /**
     * reload the database.
     *
     * @param dsId Long
     */
    @GetMapping(value = "/reloadDatabase/{dsId}")
    public ApiResult<String> reloadDatabaseById(@PathVariable(name = "dsId") Long dsId) {
        int status = schemaService.fetchAndSaveDatabaseInfo(dsId);
        if (status > 0) {
            return ApiResult.ofData(I18nMsgConfig.getMessage("ds.schema.reload.success"));
        }
        return ApiResult.ofError(StateCode.BizError, I18nMsgConfig.getMessage("ds.schema.reload.fail"));
    }

    /**
     * reload table information.
     *
     * @param dsId      Long
     * @param tableName String
     */
    @GetMapping(value = "/reload/{dsId}/{tableName}")
    public ApiResult<String> fetchTableInfo(@PathVariable(name = "dsId") Long dsId, @PathVariable(name = "tableName") String tableName) {
        List<TableColumnDO> list = schemaService.fetchAndSaveTableColumnInfo(dsId, tableName);
        if (CollectionUtils.isNotEmpty(list)) {
            return ApiResult.ofMessage(I18nMsgConfig.getMessage("ds.schema.reload.table.fail"));
        }
        return ApiResult.ofData(I18nMsgConfig.getMessage("ds.schema.reload.table.success"));
    }

    /**
     * fetch the table list
     */
    @GetMapping(value = "/fetchTableList/{dsId}")
    public ApiResult<List<TableSchemaDO>> fetchTableList(@PathVariable(name = "dsId") Long dsId) {
        List<TableSchemaDO> tableSchemaDOs = schemaService.fetchTableList(dsId);

        return ApiResult.ofData(tableSchemaDOs);
    }

    /**
     * fetch table column information list.
     */
    @GetMapping(value = "/fetchTableColumnList/{dsId}/{tableName}")
    public ApiResult<List<TableColumnDO>> fetchTableColumnList(@PathVariable(name = "dsId") Long dsId,
                                                               @PathVariable(name = "tableName") String tableName) {
        List<TableColumnDO> tableSchemaDOs = schemaService.fetchTableColumnList(dsId, tableName);

        return ApiResult.ofData(tableSchemaDOs);
    }

    /**
     * change column information.
     */
    @PostMapping(value = "/column/changeInfo")
    public ApiResult<String> changeColumnInfo(@RequestBody @Validated(value = TableColumnForm.InfoCheck.class) TableColumnForm form) {
        int status = schemaService.changeTableColumnInfo(form.toDO());
        if (status <= 0) {
            return ApiResult.ofMessage(I18nMsgConfig.getMessage("ds.schema.change.column.fail"));
        }
        return ApiResult.ofData(I18nMsgConfig.getMessage("ds.schema.change.column.success"));
    }

    /**
     * change table information.
     */
    @PostMapping(value = "/table/changeInfo")
    public ApiResult<String> changeTableInfo(@RequestBody @Validated(value = TableSchemaForm.InfoCheck.class) TableSchemaForm form) {
        int status = schemaService.updateTableSchemaInfo(form.toDO());
        if (status <= 0) {
            return ApiResult.ofMessage(I18nMsgConfig.getMessage("ds.schema.change.table.fail"));
        }
        return ApiResult.ofData(I18nMsgConfig.getMessage("ds.schema.change.table.success"));
    }
}

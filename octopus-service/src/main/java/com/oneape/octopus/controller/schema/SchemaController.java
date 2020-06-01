package com.oneape.octopus.controller.schema;

import com.google.common.base.Preconditions;
import com.oneape.octopus.model.DO.schema.DatasourceDO;
import com.oneape.octopus.model.DO.schema.TableColumnDO;
import com.oneape.octopus.model.DO.schema.TableSchemaDO;
import com.oneape.octopus.model.VO.ApiResult;
import com.oneape.octopus.service.schema.DatasourceService;
import com.oneape.octopus.service.schema.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private DatasourceService datasourceService;
    @Resource
    private SchemaService     schemaService;

    @RequestMapping(value = "/reloadDatabase/{dsId}", method = {RequestMethod.GET})
    public ApiResult reloadDatabaseById(@PathVariable(name = "dsId") Long dsId) {
        DatasourceDO ddo = Preconditions.checkNotNull(datasourceService.findById(dsId), "The data source does not exist");
        int status = schemaService.fetchAndSaveDatabaseInfo(ddo);
        if (status <= 0) {
            return ApiResult.ofMessage("Pull data source Schema fail!");
        }
        return ApiResult.ofData("Pull data source Schema successfully!");
    }

    @RequestMapping(value = "/fetchTableList/{dsId}", method = {RequestMethod.GET})
    public ApiResult<List<TableSchemaDO>> fetchTableList(@PathVariable(name = "dsId") Long dsId) {
        Preconditions.checkNotNull(datasourceService.findById(dsId), "The data source does not exist");
        List<TableSchemaDO> tableSchemaDOs = schemaService.fetchTableList(dsId);

        return ApiResult.ofData(tableSchemaDOs);
    }

    @RequestMapping(value = "/fetchTableColumnList/{dsId}/{tableName}", method = {RequestMethod.GET})
    public ApiResult<List<TableColumnDO>> fetchTableColumnList(@PathVariable(name = "dsId") Long dsId,
                                                               @PathVariable(name = "tableName") String tableName) {
        Preconditions.checkNotNull(datasourceService.findById(dsId), "The data source does not exist");
        List<TableColumnDO> tableSchemaDOs = schemaService.fetchTableColumnList(dsId, tableName);

        return ApiResult.ofData(tableSchemaDOs);
    }


}

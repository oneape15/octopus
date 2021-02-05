package com.oneape.octopus.admin.job;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.ApplicationContextProvider;
import com.oneape.octopus.domain.schema.TableColumnDO;
import com.oneape.octopus.admin.service.schema.SchemaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-29 17:10.
 * Modify:
 */
@Slf4j
@Setter
public class SchemaTableSyncJob implements Job {
    private Long   dsId;
    private String tableName;

    private SchemaService schemaService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Check the dsId value.
        Preconditions.checkArgument(dsId != null && dsId > 0, "Sync table info fail, invalid data source ID.");
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName), "Sync table info fail, invalid table name.");

        if (schemaService == null) {
            schemaService = ApplicationContextProvider.getBean(SchemaService.class);
        }

        if (schemaService == null) {
            log.error("Sync table info fail, the SchemaService uninitialized.");
            return;
        }

        List<TableColumnDO> list = null;
        try {
            list = schemaService.fetchAndSaveTableColumnInfo(dsId, tableName);
        } catch (Exception e) {
            log.error("Pulls the specified table information and saves it error!", e);
        }

        log.info("sync table info : {}-{}, {}!", dsId, tableName, CollectionUtils.isNotEmpty(list) ? "success" : "fail");

    }
}

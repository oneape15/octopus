package com.oneape.octopus.admin.job;

import com.google.common.base.Preconditions;
import com.oneape.octopus.admin.config.ApplicationContextProvider;
import com.oneape.octopus.admin.service.schema.SchemaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-29 16:55.
 * Modify:
 */
@Slf4j
@Setter
public class SchemaSyncJob implements Job {
    // The datasource id
    private Long dsId;

    private SchemaService schemaService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Check the dsId value.
        Preconditions.checkArgument(dsId != null && dsId > 0, "Sync datasource info fail, invalid data source ID.");

        if (schemaService == null) {
            schemaService = ApplicationContextProvider.getBean(SchemaService.class);
        }

        if (schemaService == null) {
            log.error("Sync datasource info fail, the SchemaService uninitialized.");
            return;
        }

        int status = 0;
        try {
            status = schemaService.fetchAndSaveDatabaseInfo(dsId);
        } catch (Exception e) {
            log.error("Pulls the specified data source information and saves it error!", e);
        }

        log.info("sync datasource : {}, {}!", dsId, status > 0 ? "success" : "fail");
    }
}

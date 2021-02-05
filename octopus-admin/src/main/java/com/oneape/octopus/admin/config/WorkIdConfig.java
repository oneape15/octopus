package com.oneape.octopus.admin.config;

import com.oneape.octopus.admin.service.uid.DefaultUidGenerator;
import com.oneape.octopus.admin.service.uid.DisposableWorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-04-30 11:36.
 * Modify:
 */
@Slf4j
@Configuration
public class WorkIdConfig {
    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        log.info("+++++++disposableWorkerIdAssigner++++");
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public DefaultUidGenerator defaultUidGenerator() {
        log.info("+++++++defaultUidGenerator++++");
        DefaultUidGenerator dug = new DefaultUidGenerator();
        dug.setWorkerIdAssigner(disposableWorkerIdAssigner());
        dug.setSeqBits(13);
        dug.setTimeBits(29);
        dug.setWorkerBits(21);
        dug.setEpochStr("2020-01-02");
        return dug;
    }
}

package com.oneape.octopus.admin.config;

import com.oneape.octopus.query.DatasourceFactory;
import com.oneape.octopus.query.DefaultDatasourceFactory;
import com.oneape.octopus.query.DefaultQueryFactory;
import com.oneape.octopus.query.QueryFactory;
import com.oneape.octopus.parse.ParseFactory;
import com.oneape.octopus.parse.xml.XmlParseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OctopusConfig {

    @Bean
    public DatasourceFactory datasourceFactory() {
        log.info("-----init octopus datasourceFactory----");
        return new DefaultDatasourceFactory();
    }

    @Bean
    public QueryFactory queryFactory() {
        log.info("-----init octopus queryFactory----");
        return new DefaultQueryFactory(datasourceFactory());
    }

    @Bean
    public ParseFactory parseFactory() {
        log.info("-----init octopus parseFactory----");
        return new XmlParseFactory();
    }
}

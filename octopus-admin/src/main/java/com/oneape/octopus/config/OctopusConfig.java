package com.oneape.octopus.config;

import com.oneape.octopus.datasource.DatasourceFactory;
import com.oneape.octopus.datasource.DefaultDatasourceFactory;
import com.oneape.octopus.datasource.DefaultQueryFactory;
import com.oneape.octopus.datasource.QueryFactory;
import com.oneape.octopus.parse.ParsingFactory;
import com.oneape.octopus.parse.xml.XmlParsingFactory;
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
    public ParsingFactory parsingFactory() {
        log.info("-----init octopus parsingFactory----");
        return new XmlParsingFactory();
    }
}

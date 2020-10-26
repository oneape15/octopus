package com.oneape.octopus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类需要和SpringBoot启动入口文件同一层
 * <p>
 * Created by oneape<oneape15@163.com>
 * Created 2020-02-17 16:25.
 * Modify:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oneape.octopus.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 获取api信息
     *
     * @return ApiInfo
     */
    private ApiInfo getApiInfo() {
        Contact contact = new Contact("oneape15", "http://www.one-ape.com", "oneape15@163.com");
        return new ApiInfoBuilder()
                .title("OCTOPUS - 数据平台")
                .description("octopus为数据产品而生")
                .version("1.0.0")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }
}
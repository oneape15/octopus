package com.oneape.octopus.api;

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
 * The Swagger2 configuration class needs to be on the same level as the SpringBoot startup entry file.
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
                .apis(RequestHandlerSelectors.basePackage("com.oneape.octopus.admin.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Get Api information.
     *
     * @return ApiInfo
     */
    private ApiInfo getApiInfo() {
        Contact contact = new Contact("oneape15", "http://www.one-ape.com", "oneape15@163.com");
        return new ApiInfoBuilder()
                .title("OCTOPUS - Data center")
                .description("octopus born for data products")
                .version("1.0.0")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }
}
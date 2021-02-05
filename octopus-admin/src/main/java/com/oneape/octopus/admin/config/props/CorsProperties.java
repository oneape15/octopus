package com.oneape.octopus.admin.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties("spring.cors")
public class CorsProperties implements Serializable {
    private List<String> allowOrigin = new ArrayList<>();
    private List<String> allowMethods = new ArrayList<>();
    private List<String> allowHeaders = new ArrayList<>();

    private boolean allowCredentials;
}

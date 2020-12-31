package com.oneape.octopus.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 16:53.
 * Modify:
 */
@Data
@Component
@ConfigurationProperties("redis")
public class RedisProperties implements Serializable {
    private String  address;
    private String  password;
    private Integer maxIdle;
    private Integer maxTotal;
    private Integer maxWaitMillis;
    private Integer timeout  = 3000;
    private Integer database = 0;
}

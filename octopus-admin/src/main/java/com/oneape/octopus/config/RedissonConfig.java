package com.oneape.octopus.config;

import com.oneape.octopus.config.props.RedisProperties;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2020-12-30 16:54.
 * Modify:
 */
@Configuration
public class RedissonConfig {
    @Resource
    private RedisProperties redisProps;

    @Bean
    public RedissonClient redissonClient() throws Exception {
        Config config = getConfig(redisProps);

        Codec codec = new JsonJacksonCodec();
        config.setCodec(codec);
        config.setThreads(2);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    // Gets the Redisson configuration.
    private Config getConfig(RedisProperties dto) {
        Config config = new Config();
        config.useSingleServer().setAddress(dto.getAddress())
                .setConnectionMinimumIdleSize(10)
                .setConnectionPoolSize(64)
                .setDatabase(dto.getDatabase())
                .setDnsMonitoringInterval(5000)
                .setSubscriptionConnectionMinimumIdleSize(1)
                .setSubscriptionConnectionPoolSize(50)
                .setSubscriptionsPerConnection(5)
                .setClientName(null)
                .setRetryAttempts(3)
                .setRetryInterval(1500)
                .setReconnectionTimeout(3000)
                .setTimeout(dto.getTimeout())
                .setIdleConnectionTimeout(10000)
                .setPingTimeout(1000)
                .setPassword(dto.getPassword());
        return config;
    }
}

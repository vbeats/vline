package com.codestepfish.vline.core.redis;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisSingleServerConfig extends RedisBaseConfig {  // 单机模式
    private String address;
    private int subscriptionConnectionMinimumIdleSize = 1;
    private int subscriptionConnectionPoolSize = 50;
    private int connectionMinimumIdleSize = 24;
    private int connectionPoolSize = 64;
    private int database = 0;
    private long dnsMonitoringInterval = 5000;
}

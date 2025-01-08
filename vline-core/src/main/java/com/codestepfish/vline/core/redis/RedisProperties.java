package com.codestepfish.vline.core.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class RedisProperties {

    RedisSingleServerConfig singleServerConfig;

    RedisClusterServersConfig clusterServersConfig;

    RedisSentinelServersConfig sentinelServersConfig;

    RedisReplicatedServersConfig redisReplicatedServersConfig;

    RedisMasterSlaveServersConfig masterSlaveServersConfig;

}

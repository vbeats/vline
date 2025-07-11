package com.codestepfish.vline.redis;

import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisClientHolder {
    public static final Map<String, RedissonClient> REDIS_CLIENTS = new ConcurrentHashMap<>(1);
}

package com.codestepfish.vline.redis.handler;

import com.codestepfish.vline.redis.RedisNode;

public interface RedisDataHandler {
    <T> void handle(RedisNode node, T data);
}

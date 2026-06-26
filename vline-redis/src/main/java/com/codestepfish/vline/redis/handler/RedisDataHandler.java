package com.codestepfish.vline.redis.handler;

import com.codestepfish.vline.redis.RedisNode;

public interface RedisDataHandler {
    void handle(RedisNode node, Object data);
}

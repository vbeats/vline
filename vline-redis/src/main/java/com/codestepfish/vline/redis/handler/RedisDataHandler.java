package com.codestepfish.vline.redis.handler;

import com.codestepfish.vline.redis.RedisNode;

public interface RedisDataHandler {

    void init(RedisNode node);

    void rec(RedisNode node, Object data);
}

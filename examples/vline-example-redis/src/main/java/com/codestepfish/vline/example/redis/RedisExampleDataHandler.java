package com.codestepfish.vline.example.redis;

import com.codestepfish.vline.redis.RedisNode;
import com.codestepfish.vline.redis.handler.RedisDataHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisExampleDataHandler implements RedisDataHandler {
    @Override
    public void handle(RedisNode node, Object data) {
        log.info("===========> node: {} rec data: {}", node.getName(), data);
    }
}

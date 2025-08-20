package com.codestepfish.vline.redis;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.redis.RedisProperties;
import com.codestepfish.vline.redis.handler.RedisDataHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class RedisNode extends Node {

    private RedisDataHandler redisDataHandler;

    @Override
    public void init() {
        super.init();

        RedisProperties properties = this.getRedis();
        try {
            Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
            Class<? extends RedisDataHandler> readHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(RedisDataHandler.class);
            redisDataHandler = readHandlerClazz.getDeclaredConstructor().newInstance();

            RedisClientHolder.REDIS_CLIENTS.put(this.getName(), Redisson.create(Config.fromYAML(ResourceUtils.getFile(String.format("classpath:redis/%s.yml", this.getName())))));
            log.info("【{}】 Redis Node Init Success , Client Mode: {}", this.getName(), properties.getMode());
        } catch (Exception e) {
            log.error("【{}】 Redis Node Init Error", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        RedisClientHolder.REDIS_CLIENTS.forEach((key, value) -> value.shutdown());
    }

    @Override
    public <T> void receiveData(T data) {
        ThreadUtil.execute(() -> redisDataHandler.handle(this, data));
    }
}

package com.codestepfish.vline.redis;

import com.codestepfish.vline.core.Node;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class RedisNode<T> extends Node<T> {
    @Override
    public void init() {
        super.init();
        Config config = new Config();


    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }

    @Override
    public void sendData(T data) {

    }
}

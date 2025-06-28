package com.codestepfish.vline.example.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;

import java.io.IOException;

@Slf4j
public class Test {
    public static void main(String[] args) throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379")
                .setDatabase(0);

        log.info("=============> config: \n {}", config.toYAML());
    }
}

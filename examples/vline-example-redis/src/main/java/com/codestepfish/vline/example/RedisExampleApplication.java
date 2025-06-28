package com.codestepfish.vline.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class RedisExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisExampleApplication.class, args);
    }
}

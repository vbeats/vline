package com.codestepfish.vlineex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VlineExMqttApplication {

    public static void main(String[] args) {
        SpringApplication.run(VlineExMqttApplication.class, args);
    }

}

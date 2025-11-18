package com.codestepfish.vline.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class H2ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(H2ExampleApplication.class, args);
    }
}

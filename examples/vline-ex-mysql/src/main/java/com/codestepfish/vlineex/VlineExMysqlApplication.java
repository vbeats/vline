package com.codestepfish.vlineex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class VlineExMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(VlineExMysqlApplication.class, args);
    }

}

package com.codestepfish.vline.core.postgres;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostgresProperties {

    private String host = "127.0.0.1";

    private Integer port = 5432;

    private String databaseName;

    private String username;

    private String password;

    private String driverClassName = "org.postgresql.Driver";

    private String jdbcUrl;  // 完整jdbc url jdbc:postgresql://host:port/database

    private String dataHandler;  // 数据处理器

    private Boolean flyway = false;  // 是否开启flyway
}

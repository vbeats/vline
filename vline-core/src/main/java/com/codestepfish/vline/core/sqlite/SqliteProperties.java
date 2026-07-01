package com.codestepfish.vline.core.sqlite;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SqliteProperties {

    private String dbPath; // db 文件路径

    private String driverClassName = "org.sqlite.JDBC";

    private String jdbcUrl;  // 完整jdbc url jdbc:sqlite:xx.db

    private Boolean flyway = false;  // 是否开启flyway
}

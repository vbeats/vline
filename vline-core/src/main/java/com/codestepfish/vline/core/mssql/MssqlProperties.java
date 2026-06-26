package com.codestepfish.vline.core.mssql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MssqlProperties {

    private String host = "127.0.0.1";

    private Integer port = 1433;

    private String databaseName;

    private String username;

    private String password;

    private Boolean encrypt = false;

    private Boolean trustServerCertificate = true;

    private String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private String jdbcUrl;  // 完整jdbc url

    private String dataHandler;  // 数据处理器

    private Boolean flyway = false;  // 是否开启flyway
}

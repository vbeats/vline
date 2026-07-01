package com.codestepfish.vline.core.oracle;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OracleProperties {

    private String host = "127.0.0.1";

    private Integer port = 1521;

    private String serviceName;

    private String username;

    private String password;

    private String driverClassName = "oracle.jdbc.driver.OracleDriver";

    private String jdbcUrl;  // 完整jdbc url jdbc:oracle:thin:@host:port:serviceName

    private Boolean flyway = false;  // 是否开启flyway
}

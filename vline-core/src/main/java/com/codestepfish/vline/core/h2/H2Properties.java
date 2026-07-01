package com.codestepfish.vline.core.h2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class H2Properties {

    private String driverClassName = "org.h2.Driver";

    private String jdbcUrl;  // 完整jdbc url jdbc:h2:mem:testdb

    private String username;

    private String password;

    private Boolean flyway = false;  // 是否开启flyway
}

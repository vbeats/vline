package com.codestepfish.vline.core.postgres;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostgresProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.OTHER;  // read or write

    private String host = "127.0.0.1";

    private Integer port = 5432;

    private String databaseName;

    private String username;

    private String password;

    private String driverClassName = "org.postgresql.Driver";

    private String jdbcUrl;  // 完整jdbc url jdbc:postgresql://host:port/database

    private String dataHandler;  // 数据处理器

    public enum Mode {
        READ,
        WRITE,
        OTHER  // other 不处理任何业务, 仅注入数据源
    }
}

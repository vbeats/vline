package com.codestepfish.vline.core.mssql;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MssqlProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.OTHER;  // read or write

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

    public enum Mode {
        READ,
        WRITE,
        OTHER  // other 不处理任何业务, 仅注入数据源
    }
}

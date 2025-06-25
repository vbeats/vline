package com.codestepfish.vline.core.sqlite;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SqliteProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.OTHER;  // read or write

    private String dbPath; // db 文件路径

    private String databaseName;

    private String driverClassName = "org.sqlite.JDBC";

    private String jdbcUrl;  // 完整jdbc url jdbc:sqlite://xx.db

    private String dataHandler;  // 数据处理器

    public enum Mode {
        READ,
        WRITE,
        OTHER  // other 不处理任何业务, 仅注入数据源
    }
}

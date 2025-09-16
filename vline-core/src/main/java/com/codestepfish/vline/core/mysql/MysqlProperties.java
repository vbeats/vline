package com.codestepfish.vline.core.mysql;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MysqlProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.OTHER;  // read or write

    private String host = "127.0.0.1";

    private Integer port = 3306;

    private String databaseName;

    private String username;

    private String password;

    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    private String jdbcUrl;  // 完整jdbc url jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true

    private String dataHandler;  // 数据处理器

    private Boolean flyway = false;  // 是否开启flyway

    public enum Mode {
        READ,
        WRITE,
        OTHER  // other 不处理任何业务, 仅注入数据源
    }
}

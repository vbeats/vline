package com.codestepfish.vline.core.oracle;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OracleProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.OTHER;  // read or write

    private String host = "127.0.0.1";

    private Integer port = 1521;

    private String serviceName;

    private String username;

    private String password;

    private String driverClassName = "oracle.jdbc.driver.OracleDriver";

    private String jdbcUrl;  // 完整jdbc url jdbc:oracle:thin:@host:port:serviceName

    private String dataHandler;  // 数据处理器

    private Boolean flyway = false;  // 是否开启flyway

    public enum Mode {
        READ,
        WRITE,
        OTHER  // other 不处理任何业务, 仅注入数据源
    }
}

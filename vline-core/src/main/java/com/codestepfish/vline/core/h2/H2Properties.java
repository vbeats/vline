package com.codestepfish.vline.core.h2;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class H2Properties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private H2Properties.Mode mode = H2Properties.Mode.OTHER;  // read or write

    private String driverClassName = "org.h2.Driver";

    private String jdbcUrl;  // 完整jdbc url jdbc:h2:mem:testdb

    private String username;

    private String password;

    private String dataHandler;  // 数据处理器

    private Boolean flyway = false;  // 是否开启flyway

    public enum Mode {
        READ,
        WRITE,
        OTHER  // other 不处理任何业务, 仅注入数据源
    }
}

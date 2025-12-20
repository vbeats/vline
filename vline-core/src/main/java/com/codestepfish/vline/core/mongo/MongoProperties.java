package com.codestepfish.vline.core.mongo;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MongoProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.OTHER;  // read or write

    private String uri;  // 连接uri mongodb://localhost:27017

    private String dataHandler;  // 数据处理器

    public enum Mode {
        READ,
        WRITE,
        OTHER  // other 不处理任何业务, 仅注入数据源
    }
}

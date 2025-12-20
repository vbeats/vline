package com.codestepfish.vline.core.duckdb;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DuckProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)

    private String uri;  // 完整duckdb uri jdbc:duckdb:

    private String dataHandler;  // 数据处理器
}

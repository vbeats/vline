package com.codestepfish.vline.core.duckdb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DuckProperties {

    private String uri;  // 完整duckdb uri jdbc:duckdb:

    private String dataHandler;  // 数据处理器
}

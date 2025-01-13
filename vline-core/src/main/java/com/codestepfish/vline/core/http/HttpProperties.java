package com.codestepfish.vline.core.http;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HttpProperties {
    private String handler; // http请求处理器  实现com.codestepfish.vline.http.HttpHandler
}

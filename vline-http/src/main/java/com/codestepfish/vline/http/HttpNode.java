package com.codestepfish.vline.http;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.http.HttpProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class HttpNode<T> extends Node<T> {
    @Override
    public void init() {
        super.init();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void sendMsg(T msg) {
        HttpProperties httpProperties = this.getHttp();
        String url = httpProperties.getUrl();
        HttpRequest httpRequest;

        switch (httpProperties.getMethod()) {
            case GET -> httpRequest = HttpRequest.get(url);
            case POST -> httpRequest = HttpRequest.post(url);
            default -> throw new RuntimeException("unSupport protocol...");
        }

        HttpResponse response = httpRequest.body(JSON.toJSONString(new HashMap<>() {{
            put("msg", msg);
        }})).contentType(ContentType.JSON.getValue()).executeAsync();

        log.info("response: {} -- {}", response.getStatus(), response.body());
    }
}

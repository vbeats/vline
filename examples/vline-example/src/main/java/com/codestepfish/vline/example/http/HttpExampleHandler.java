package com.codestepfish.vline.example.http;

import com.codestepfish.vline.core.http.HttpProperties;
import com.codestepfish.vline.http.client.ForestHandler;
import com.dtflys.forest.http.ForestRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpExampleHandler<T> extends ForestHandler<T> {
    @Override
    public void handle(ForestRequest request, HttpProperties properties, T data) {
        log.info("custom http handle: {} {} {}", request, properties, data);
    }
}

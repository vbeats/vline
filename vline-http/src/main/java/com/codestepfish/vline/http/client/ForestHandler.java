package com.codestepfish.vline.http.client;

import com.codestepfish.vline.core.http.HttpProperties;
import com.dtflys.forest.http.ForestRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ForestHandler<T> {
    // 上层重写此方法 处理具体请求逻辑
    public void handle(ForestRequest request, HttpProperties properties, T data) {
        // 默认处理逻辑
        request.contentTypeJson()
                .addBody((String) data)
                .execute();
    }

}

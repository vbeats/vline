package com.codestepfish.vlineex.api;

import com.codestepfish.vline.http.HttpHandler;
import com.codestepfish.vline.http.HttpNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiHandler implements HttpHandler<Object> {

    @Override
    public void handle(HttpNode node, Object data) {

        log.info("=========> 【Request】 数据推送请求: {}", new String((byte[]) data));

    }
}

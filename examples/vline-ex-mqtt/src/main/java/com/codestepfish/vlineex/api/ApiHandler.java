package com.codestepfish.vlineex.api;

import com.codestepfish.vline.http.HttpHandler;
import com.codestepfish.vline.http.HttpNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiHandler implements HttpHandler {

    @Override
    public void init(HttpNode node) {

    }

    @Override
    public void rec(HttpNode node, Object data) {

        log.info("=========> 【Request】 数据推送请求: {}", data);

    }

    @Override
    public void destroy(HttpNode node) {

    }

}

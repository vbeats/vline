package com.codestepfish.vline.example.http;

import com.codestepfish.vline.http.HttpHandler;
import com.codestepfish.vline.http.HttpNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HttpExampleHandler implements HttpHandler<Object> {

    @Override
    public void handle(HttpNode node, Object data) {
        log.info("custom http handle: {} {} ", node, data);
    }
}

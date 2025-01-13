package com.codestepfish.vline.http;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.http.HttpProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class HttpNode<T> extends Node<T> {

    private HttpHandler httpHandler;

    @Override
    public void init() {
        super.init();
        // 初始化forest client
        ThreadUtil.execute(() -> {
            HttpProperties hp = this.getHttp();

            try {
                Assert.hasText(hp.getHandler(), "http handler is null");

                this.httpHandler = SpringUtil.getBean(hp.getHandler());

            } catch (Exception e) {
                log.error("http forest init failed : ", e);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void destroy() {
        log.info("node destroy: {}", this.getName());
    }

    @Override
    public void sendData(T data) {
        this.httpHandler.handle(this, data);
    }
}

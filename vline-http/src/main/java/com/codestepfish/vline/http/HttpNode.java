package com.codestepfish.vline.http;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.http.HttpProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class HttpNode extends Node {

    private HttpHandler httpHandler;

    @Override
    public void init() {
        super.init();
        // 初始化forest client
        Thread.ofVirtual().start(() -> {
            HttpProperties hp = this.getHttp();

            try {
                Assert.hasText(hp.getHandler(), "【" + this.getName() + "】 Require Config HttpHandler");

                httpHandler = SpringUtil.getBean(HttpHandler.class);

            } catch (Exception e) {
                log.error("【{}】 Init Failed : ", this.getName(), e);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void receiveData(Object data) {
        httpHandler.handle(this, data);
    }
}

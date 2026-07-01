package com.codestepfish.vline.http;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

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
        try {
            httpHandler = SpringUtil.getBean(HttpHandler.class);

            httpHandler.init(this);
        } catch (Exception e) {
            log.error("【{}】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveData(Object data) {
        httpHandler.rec(this, data);
    }
}

package com.codestepfish.vline.spring.boot.starter.eventbus;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.eventbus.VLineEvent;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.codestepfish.vline.spring.boot.starter.VLineProperties;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class VLineEventListener implements SubscriberExceptionHandler { // event bus event msg listener

    @Subscribe
    public void msgHandler(VLineEvent event) {
        log.info("event bus received event: {}", event);
        List<String> nextNodes = SpringUtil.getBean(VLineProperties.class).nextNodes(event.getKey());
        nextNodes.parallelStream().forEach(node -> VLineContext.NODES.get(node).sendData(event.getMsg()));
    }

    @Subscribe
    public void deadEventHandler(DeadEvent event) {
        log.warn("event bus dead event : {}", event);
    }

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error("event bus : {} exception : ", context, exception);
    }
}

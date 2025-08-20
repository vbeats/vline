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
    public <T> void msgHandler(VLineEvent<T> event) {
        log.info("【 {} 】 Event Bus Received Event : {}", event.getKey(), event);
        List<String> nextNodes = SpringUtil.getBean(VLineProperties.class).nextNodes(event.getKey());
        nextNodes.parallelStream().forEach(node -> VLineContext.NODES.get(node).receiveData(event.getMsg()));
    }

    @Subscribe
    public void deadEventHandler(DeadEvent event) {
        log.warn("Event Bus Dead Event : {}", event);
    }

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error("Event Bus {} Exception : ", context, exception);
    }
}

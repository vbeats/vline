package com.codestepfish.vline.spring.boot.starter;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.eventbus.VLineEvent;
import com.lmax.disruptor.RingBuffer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Slf4j
@Component
@RequiredArgsConstructor
public class VLineContext {

    // key node name
    public static final Map<String, Node> NODES = new ConcurrentHashMap<>(10);

    private static volatile RingBuffer<VLineEvent> eventBus;

    // 推送消息 --> event bus
    public static void posMsg(String nodeName, Object payload) {
        if (eventBus == null) {
            synchronized (VLineContext.class) {
                eventBus = SpringUtil.getBean(new TypeReference<>() {
                });
            }
        }

        long sequence = eventBus.next();
        try {
            VLineEvent event = eventBus.get(sequence);
            event.setKey(nodeName);
            event.setPayload(payload);
        } finally {
            eventBus.publish(sequence);
        }
    }

}

package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.eventbus.VLineEvent;
import com.codestepfish.vline.spring.boot.starter.eventbus.VLineEventListener;
import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Slf4j
public class VLineContext {

    // key node name
    public static final Map<String, EventBus> EVENT_BUS_MAP = new ConcurrentHashMap<>(10);
    public static final Map<String, Node> NODES = new ConcurrentHashMap<>(10);

    /**
     * 创建eventbus
     *
     * @param key 默认topo struct 入口
     */
    public static void createEventBus(String key) {
        EventBus eventBus = new EventBus(key);
        eventBus.register(new VLineEventListener());
        EVENT_BUS_MAP.put(key, eventBus);
    }

    // 推送消息 --> event bus
    public static <T> void posMsg(String key, T msg) {
        EVENT_BUS_MAP.get(key).post(new VLineEvent<>(key, msg));
    }

}

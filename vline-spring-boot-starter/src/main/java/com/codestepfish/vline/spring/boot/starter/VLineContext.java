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
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline.spring.boot.starter")
public class VLineContext {

    // key node name
    public static final Map<String, Node> NODES = new ConcurrentHashMap<>(10);

    private final VLineProperties vLineProperties;
    private static volatile RingBuffer<VLineEvent> eventBus;

    /**
     * 当前node的 下级/上级节点 名
     *
     * @param nodeName
     * @return
     */
    @Cacheable(value = "vline:nodes", key = "#nodeName", unless = "#result == null || #result.size()==0")
    public List<String> nextNodes(String nodeName) {

        Map<String, List<String>> struct = vLineProperties.getStruct();

        if (CollectionUtils.isEmpty(struct)) {
            return Collections.emptyList();
        }
        List<String> ns = new ArrayList<>();

        struct.forEach((key, value) -> {
            if (nodeName.equals(key)) {
                ns.addAll(value);
            }

            if (value.contains(nodeName)) {
                ns.add(key);
            }
        });

        return ns;
    }

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

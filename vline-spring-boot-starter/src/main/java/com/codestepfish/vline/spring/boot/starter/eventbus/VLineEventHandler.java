package com.codestepfish.vline.spring.boot.starter.eventbus;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.eventbus.VLineEvent;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.codestepfish.vline.spring.boot.starter.VLineProperties;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Component
public class VLineEventHandler implements EventHandler<VLineEvent> {

    @Override
    public void onEvent(VLineEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (ObjectUtils.isEmpty(event.getPayload())) {
            return;
        }
        log.info("【 {} 】 -- {} EventHandler Received Data : {}", event.getKey(), sequence, event.getPayload());
        List<String> nextNodes = SpringUtil.getBean(VLineProperties.class).nextNodes(event.getKey());
        nextNodes.parallelStream().forEach(node -> VLineContext.NODES.get(node).receiveData(event.getPayload()));
    }
}

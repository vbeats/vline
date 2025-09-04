package com.codestepfish.vline.spring.boot.starter.eventbus;

import com.codestepfish.vline.core.eventbus.VLineEvent;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.lmax.disruptor.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VLineEventHandler implements EventHandler<VLineEvent> {

    private final VLineContext vLineContext;

    @Override
    public void onEvent(VLineEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("【 {} 】 -- {} EventHandler Received Data : {}", event.getKey(), sequence, event.getPayload());
        List<String> nextNodes = vLineContext.nextNodes(event.getKey());
        nextNodes.parallelStream().forEach(node -> VLineContext.NODES.get(node).receiveData(event.getPayload()));
    }
}

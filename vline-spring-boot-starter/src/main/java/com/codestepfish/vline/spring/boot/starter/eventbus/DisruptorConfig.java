package com.codestepfish.vline.spring.boot.starter.eventbus;

import com.codestepfish.vline.core.eventbus.VLineEvent;
import com.codestepfish.vline.core.eventbus.VLineEventFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@RequiredArgsConstructor
public class DisruptorConfig {

    private final VLineEventHandler vLineEventHandler;

    @Bean(destroyMethod = "shutdown")
    public Disruptor<VLineEvent> disruptor() {

        int ringBufferSize = 1024;  // adjust (producer/consumer speed)

       /* ThreadFactory threadFactory = r -> {
            thread.setName("disruptor-" + new AtomicInteger().incrementAndGet());
            thread.setDaemon(true);
            return thread;
        };*/

        /*ThreadFactory threadFactory = Thread.ofPlatform().name("disruptor-" + new AtomicInteger().incrementAndGet())
                .daemon(true).factory();*/

        // virtual thread 👍
        ThreadFactory threadFactory = Thread.ofVirtual().name("disruptor-" + new AtomicInteger().incrementAndGet()).factory();

        Disruptor<VLineEvent> disruptor = new Disruptor<>(new VLineEventFactory(), ringBufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());

        // event handler
        disruptor.handleEventsWith(vLineEventHandler);

        disruptor.start();

        return disruptor;
    }

    @Bean
    public RingBuffer<VLineEvent> eventBus(Disruptor<VLineEvent> disruptor) {
        return disruptor.getRingBuffer();
    }
}

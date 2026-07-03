package com.codestepfish.vline.serialport;

import cn.hutool.extra.spring.SpringUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.serialport.handler.SerialPortDataHandler;
import com.codestepfish.vline.serialport.handler.SerialPortHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.util.concurrent.RateLimiter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class SerialPortNode extends Node {

    public static final Map<String, RateLimiter> RATE_LIMITERS = new ConcurrentHashMap<>(10);  // 每个串口一个rateLimiter
    private SerialPortDataHandler serialPortDataHandler;

    @Override
    public void init() {
        super.init();
        try {
            serialPortDataHandler = SpringUtil.getBean(SerialPortDataHandler.class);

            RATE_LIMITERS.put(this.getName(), RateLimiter.create(1, 2L, TimeUnit.SECONDS));

            // 初始化串口
            Thread.ofVirtual().start(() -> SerialPortHandler.openPort(this, serialPortDataHandler));
        } catch (Exception e) {
            log.error("【{}】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        serialPortDataHandler.destroy(this);
        SerialPortHandler.SERIAL_PORTS.entrySet().parallelStream()
                .forEach(entry -> entry.getValue().closePort());
    }

    @Override
    public void receiveData(Object data) {
        serialPortDataHandler.send(this, data);
    }
}

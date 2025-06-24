package com.codestepfish.vline.serialport;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.serialport.handler.SerialPortDataHandler;
import com.codestepfish.vline.serialport.handler.SerialPortHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class SerialPortNode<T> extends Node<T> {

    SerialPortDataHandler serialPortDataHandler;

    @Override
    public void init() {
        super.init();
        try {
            Assert.hasText(this.getSerialPort().getDataHandler(), "serial port dataHandler is null");

            Class<? extends SerialPortDataHandler> dataHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(this.getSerialPort().getDataHandler()).asSubclass(SerialPortDataHandler.class);
            serialPortDataHandler = dataHandlerClazz.getDeclaredConstructor().newInstance();
            // 初始化串口
            ThreadUtil.execute(() -> SerialPortHandler.init(this, serialPortDataHandler));
        } catch (Exception e) {
            log.error("serial port : {} init failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        SerialPortHandler.SERIAL_PORTS.entrySet().parallelStream()
                .forEach(entry -> entry.getValue().closePort());
    }

    @Override
    public void receiveData(T data) {
        ThreadUtil.execute(() -> serialPortDataHandler.send(this, data));
    }
}

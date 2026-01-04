package com.codestepfish.vline.service;

import cn.hutool.core.thread.ThreadUtil;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class SerialPortDataHandler implements InitializingBean, DisposableBean {

    private final DataService dataService;

    public static final Map<String, RateLimiter> RATE_LIMITERS = new ConcurrentHashMap<>(10);  // 每个串口一个rateLimiter
    // 枪号 串口
    public static final ConcurrentHashMap<String, SerialPort> DEVICES = new ConcurrentHashMap<>();

    @Override
    public void destroy() throws Exception {
        SerialPortDataHandler.DEVICES.entrySet().parallelStream()
                .forEach(entry -> entry.getValue().closePort());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化串口
        List<String> ds = Arrays.asList("COM1", "COM4");

        ds.forEach(device -> {
            RATE_LIMITERS.put(device, RateLimiter.create(1, 2L, TimeUnit.SECONDS));

            ThreadUtil.execAsync(() -> openPort(device));
        });
    }

    private void openPort(String device) {
        try {
            SerialPort serialPort = SerialPort.getCommPort(device);

            serialPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY, false);

            log.info("【{}】 Try Open ...", device);

            boolean opened = serialPort.openPort();
            if (!opened) {
                reOpenPort(device);
            }

            log.info("【{}】 Open Success... Data Listening...", device);

            DEVICES.put(device, serialPort);

            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
                }

                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {

                    switch (serialPortEvent.getEventType()) {
                        case SerialPort.LISTENING_EVENT_DATA_AVAILABLE:
                            dataService.handleData(device);
                            break;
                        case SerialPort.LISTENING_EVENT_PORT_DISCONNECTED:
                            reOpenPort(device);
                            break;
                    }

                }
            });

        } catch (SerialPortInvalidPortException e) {
            log.warn("【{}】 Invalid Device or Not Connected...", device);
            reOpenPort(device);
        } catch (Exception e) {
            log.error("【{}】 Open Exception...", device, e);
        }
    }

    private void reOpenPort(String device) {
        if (RATE_LIMITERS.get(device).tryAcquire(3, 5L, TimeUnit.SECONDS)) {
            log.warn("【{}】 Disconnect....Try Reconnect...", device);
            SerialPort serialPort = DEVICES.get(device);
            if (!ObjectUtils.isEmpty(serialPort) && serialPort.isOpen()) {
                serialPort.closePort();
            }

            // 打开串口
            openPort(device);
        }
    }
}

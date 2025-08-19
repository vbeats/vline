package com.codestepfish.vline.serialport.handler;

import com.codestepfish.vline.core.serialport.SerialPortProperties;
import com.codestepfish.vline.serialport.SerialPortNode;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.codestepfish.vline.serialport.SerialPortNode.RATE_LIMITERS;

@Slf4j
public class SerialPortHandler {

    public static final Map<String, SerialPort> SERIAL_PORTS = new ConcurrentHashMap<>(10);

    public static <T> boolean openPort(SerialPortNode node, SerialPortDataHandler dataHandler) {
        SerialPortProperties properties = node.getSerialPort();

        SerialPort serialPort = SerialPort.getCommPort(properties.getDevice());

        serialPort.setComPortParameters(properties.getBaudRate(), properties.getDataBits(), properties.getStopBits(), properties.getParity(), properties.getUseRs485Mode());

        log.info("===========> 准备打开 【{}】 ...", properties.getDevice());

        boolean opened = serialPort.openPort();
        if (!opened) {
            reOpenPort(node, dataHandler);
            return false;
        }

        log.info("===========> 【{}】 Open Success... 开始监听数据...", properties.getDevice());

        SERIAL_PORTS.put(node.getName(), serialPort);

        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE | SerialPort.LISTENING_EVENT_PORT_DISCONNECTED;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {

                switch (serialPortEvent.getEventType()) {
                    case SerialPort.LISTENING_EVENT_DATA_AVAILABLE -> dataHandler.receive(node);
                    case SerialPort.LISTENING_EVENT_PORT_DISCONNECTED -> reOpenPort(node, dataHandler);
                }
            }
        });

        return true;
    }

    // 重新打开串口  5s内3个令牌
    private static void reOpenPort(SerialPortNode node, SerialPortDataHandler dataHandler) {
        if (RATE_LIMITERS.get(node.getName()).tryAcquire(3, 5L, TimeUnit.SECONDS)) {
            log.info("===========> 【{}】 disconnect.... 3s后尝试重新连接", node.getSerialPort().getDevice());
            SerialPort serialPort = SERIAL_PORTS.get(node.getName());
            if (!ObjectUtils.isEmpty(serialPort) && serialPort.isOpen()) {
                serialPort.closePort();
            }

            // 打开串口
            openPort(node, dataHandler);
        }
    }
}

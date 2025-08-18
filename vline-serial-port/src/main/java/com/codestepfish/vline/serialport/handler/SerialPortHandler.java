package com.codestepfish.vline.serialport.handler;

import com.codestepfish.vline.core.serialport.SerialPortProperties;
import com.codestepfish.vline.serialport.SerialPortNode;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SerialPortHandler {

    public static final Map<String, SerialPort> SERIAL_PORTS = new ConcurrentHashMap<>(10);


    public static <T> void init(SerialPortNode node, SerialPortDataHandler dataHandler) {
        SerialPortProperties properties = node.getSerialPort();

        SerialPort serialPort = SerialPort.getCommPort(properties.getDevice());

        serialPort.setComPortParameters(properties.getBaudRate(), properties.getDataBits(), properties.getStopBits(), properties.getParity(), properties.getUseRs485Mode());

        boolean opened = serialPort.openPort();
        log.info("===========> 串口: {} 打开状态: {}", properties.getDevice(), opened);
        if (!opened) {
            log.error("串口: {} 打开失败", properties.getDevice());
            return;
        }

        SERIAL_PORTS.put(node.getName(), serialPort);

        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {

                if (SerialPort.LISTENING_EVENT_DATA_AVAILABLE != serialPortEvent.getEventType()) {
                    return;
                }

                // 接收数据
                dataHandler.receive(node);
            }
        });
    }
}

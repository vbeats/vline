package com.codestepfish.vline.serialport.handler;

import com.codestepfish.vline.serialport.SerialPortNode;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SerialPortHandler {

    public static final Map<String, SerialPort> SERIAL_PORTS = new ConcurrentHashMap<>(10);


    public static <T> void init(SerialPortNode node, SerialPortDataHandler dataHandler) {

        SerialPort serialPort = SerialPort.getCommPort(node.getSerialPort().getDevice());
        boolean opened = serialPort.openPort();
        log.info("===========> 串口: {} 打开结果: {}", node.getSerialPort().getDevice(), opened);
        if (!opened) {
            throw new RuntimeException("串口: " + node.getSerialPort().getDevice() + " 打开失败");
        }

        SERIAL_PORTS.put(node.getName(), serialPort);

        // 监听数据

        dataHandler.receive(node);
    }
}

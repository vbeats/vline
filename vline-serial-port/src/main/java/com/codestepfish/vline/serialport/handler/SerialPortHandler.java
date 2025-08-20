package com.codestepfish.vline.serialport.handler;

import com.codestepfish.vline.core.serialport.SerialPortProperties;
import com.codestepfish.vline.serialport.SerialPortNode;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.codestepfish.vline.serialport.SerialPortNode.RATE_LIMITERS;

@Slf4j
public class SerialPortHandler {

    public static final Map<String, SerialPort> SERIAL_PORTS = new ConcurrentHashMap<>(10);

    public static <T> void openPort(SerialPortNode node, SerialPortDataHandler dataHandler) {

        SerialPortProperties properties = node.getSerialPort();

        try {
            SerialPort serialPort = SerialPort.getCommPort(properties.getDevice());

            serialPort.setComPortParameters(properties.getBaudRate(), properties.getDataBits(), properties.getStopBits(), properties.getParity(), properties.getUseRs485Mode());

            log.info("【{}】 Try Open ...", properties.getDevice());

            boolean opened = serialPort.openPort();
            if (!opened) {
                reOpenPort(node, dataHandler);
            }

            log.info("【{}】 Open Success... Data Listening...", properties.getDevice());

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

        } catch (SerialPortInvalidPortException e) {
            log.warn("【{}】 Invalid Device or Not Connected...", properties.getDevice());
            reOpenPort(node, dataHandler);
        } catch (Exception e) {
            log.error("【{}】 Open Exception...", properties.getDevice(), e);
        }
    }

    // 重新打开串口  5s内3个令牌
    protected static void reOpenPort(SerialPortNode node, SerialPortDataHandler dataHandler) {
        if (RATE_LIMITERS.get(node.getName()).tryAcquire(3, 5L, TimeUnit.SECONDS)) {
            log.warn("【{}】 Disconnect....Try Reconnect...", node.getSerialPort().getDevice());
            SerialPort serialPort = SERIAL_PORTS.get(node.getName());
            if (!ObjectUtils.isEmpty(serialPort) && serialPort.isOpen()) {
                serialPort.closePort();
            }

            // 打开串口
            openPort(node, dataHandler);
        }
    }
}

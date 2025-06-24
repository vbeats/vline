package com.codestepfish.vline.example.serialport;

import com.codestepfish.vline.serialport.SerialPortNode;
import com.codestepfish.vline.serialport.handler.SerialPortDataHandler;
import com.codestepfish.vline.serialport.handler.SerialPortHandler;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerialPortExampleDataHandler implements SerialPortDataHandler {


    @Override
    public <T> void receive(SerialPortNode node) {

        SerialPort serialPort = SerialPortHandler.SERIAL_PORTS.get(node.getName());

        while (true) {
            while (serialPort.bytesAvailable() > 0) {
                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                log.info("=======> {}【{}】 receive data: {}", node.getName(), node.getSerialPort().getDevice(), bytesToHex(readBuffer));

                VLineContext.posMsg(node.getName(), readBuffer);
            }
        }
    }

    @Override
    public <T> void send(SerialPortNode node, T data) {

        // data ===> send to node

        byte[] datas = (byte[]) data;

        SerialPort serialPort = SerialPortHandler.SERIAL_PORTS.get(node.getName());
        serialPort.writeBytes(datas, datas.length);

    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}

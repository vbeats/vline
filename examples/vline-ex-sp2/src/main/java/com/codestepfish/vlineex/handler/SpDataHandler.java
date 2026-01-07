package com.codestepfish.vlineex.handler;

import cn.hutool.core.util.RandomUtil;
import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.serialport.SerialPortNode;
import com.codestepfish.vline.serialport.handler.SerialPortDataHandler;
import com.codestepfish.vline.serialport.handler.SerialPortHandler;
import com.codestepfish.vlineex.model.DataItem;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpDataHandler implements SerialPortDataHandler {

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%x", b));
        }
        return result.toString();
    }

    @Override
    public void receive(SerialPortNode node) {

        SerialPort serialPort = SerialPortHandler.SERIAL_PORTS.get(node.getName());
        int bytesAvailable = serialPort.bytesAvailable();
        if (bytesAvailable <= 0) {
            return;
        }
        byte[] readBuffer = new byte[bytesAvailable];
        serialPort.readBytes(readBuffer, readBuffer.length);

        String data = bytesToHex(readBuffer);
        log.info("=======> {}【{}】 receive data: {} {}", node.getName(), node.getSerialPort().getDevice(), data, new String(readBuffer));

        VLineContext.posMsg(node.getName(), DataItem.builder().id(RandomUtil.randomLong()).build());
    }

    @Override
    public <T> void send(SerialPortNode node, T data) {
    }
}

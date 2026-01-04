package com.codestepfish.vline.service;

import com.fazecast.jSerialComm.SerialPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataService {

    public void handleData(String device) {
        SerialPort serialPort = SerialPortDataHandler.DEVICES.get(device);
        int bytesAvailable = serialPort.bytesAvailable();
        if (bytesAvailable <= 0) {
            return;
        }
        byte[] readBuffer = new byte[bytesAvailable];
        serialPort.readBytes(readBuffer, readBuffer.length);

        String data = bytesToHex(readBuffer);
        log.info("=======>【{}】 receive data: {} -- {}", device, data, new String(readBuffer));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%x", b));
        }
        return result.toString();
    }

}

package com.codestepfish.vline.serialport.handler;

import com.codestepfish.vline.serialport.SerialPortNode;

public interface SerialPortDataHandler {

    void receive(SerialPortNode node);

    <T> void send(SerialPortNode node, T data);
}

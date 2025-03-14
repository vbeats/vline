package com.codestepfish.vline.serialport.handler;

import com.codestepfish.vline.serialport.SerialPortNode;

public interface SerialPortDataHandler {

    <T> void receive(SerialPortNode node);

    <T> void send(SerialPortNode node, T data);
}

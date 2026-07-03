package com.codestepfish.vline.serialport.handler;

import com.codestepfish.vline.serialport.SerialPortNode;

public interface SerialPortDataHandler {

    void receive(SerialPortNode node);

    void send(SerialPortNode node, Object data);

    void destroy(SerialPortNode node);
}

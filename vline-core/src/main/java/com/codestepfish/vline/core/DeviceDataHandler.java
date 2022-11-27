package com.codestepfish.vline.core;

public interface DeviceDataHandler {

    /**
     * 收到数据
     */
    void onReceiveData();

    /**
     * 发送数据
     */
    void sendData();
}

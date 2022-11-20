package com.codestepfish.vline.core;

public interface IDevice {

    /**
     * 设备通信初始化
     */
    void init();

    /**
     * 设备通信资源销毁
     */
    void destroy();

    /**
     * 接收数据
     */
    void receive();

    /**
     * send data
     */
    void send();
}

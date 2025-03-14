package com.codestepfish.vline.core.serialport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SerialPortProperties {

    // 串口 属性
    private String device;  // 设备

    private Boolean ignored = true; // 忽略其它业务处理  只转发数据

    private String dataHandler;  // 业务数据处理
}

package com.codestepfish.vline.core;

import cn.hutool.http.Method;
import com.codestepfish.vline.core.codec.VLineCodec;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class Device implements IDevice, Serializable {

    String name;

    /**
     * 设备通信类型
     */
    DeviceType type;

    String url;

    String host;

    Integer port;

    Integer database;

    Method method;

    VLineCodec codec;

    /**
     * 出口
     */
    List<Device> out;

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void receive() {

    }

    @Override
    public void send() {

    }

    public enum DeviceType {
        TCP,
        HTTP,
        ETCD,
        GRPC,
        NACOS,
        REDIS,
        MQTT,
        DATABASE,
        SERIAL_PORT
    }
}



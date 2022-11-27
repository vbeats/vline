package com.codestepfish.vline.core;

import cn.hutool.http.Method;
import com.codestepfish.vline.core.codec.VLineCodec;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Device implements IDevice, Serializable {

    String name;

    List<String> tags;

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

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    public enum DeviceType {
        TCP,
        HTTP,
        ETCD,
        GRPC,
        REDIS,
        MQTT,
        DATABASE,
        SERIAL_PORT
    }
}



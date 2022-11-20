package com.codestepfish.vline.redis;

import com.codestepfish.vline.core.Device;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
public class RedisDevice extends Device {

    private String host;

    private Integer port;

    private Integer database = 0;

    private String password;

    private DeviceType type = DeviceType.REDIS;

    private List<Device> out;

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
}

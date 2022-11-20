package com.codestepfish.vline.http;

import cn.hutool.http.Method;
import com.codestepfish.vline.core.Device;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@Slf4j
public class HttpDevice extends Device {

    private String url;

    private DeviceType type = DeviceType.HTTP;

    private String method = Method.GET.toString();

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

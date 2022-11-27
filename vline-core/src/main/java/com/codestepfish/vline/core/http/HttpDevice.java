package com.codestepfish.vline.core.http;

import com.codestepfish.vline.core.Device;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@Accessors(chain = true)
public class HttpDevice extends Device {

    private DeviceType type = DeviceType.HTTP;

    @Override
    public void init() {
        log.info("http device start init....");
        log.info("http device init success ....");

    }

    @Override
    public void destroy() {
        log.info("http device start destroy....");
        log.info("http device destroy success ....");
    }

}

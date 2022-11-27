package com.codestepfish.vline.core.redis;

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
public class RedisDevice extends Device {

    private DeviceType type = DeviceType.REDIS;

    @Override
    public void init() {
        log.info("redis device start init....");
        log.info("redis device init success ....");
    }

    @Override
    public void destroy() {
        log.info("redis device start destroy....");
        log.info("redis device destroy success ....");
    }

}

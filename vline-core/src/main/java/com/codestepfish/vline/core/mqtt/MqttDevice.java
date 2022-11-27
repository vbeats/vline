package com.codestepfish.vline.core.mqtt;

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
public class MqttDevice extends Device {
    @Override
    public void init() {
        log.info("mqtt device start init....");
        log.info("mqtt device init success ....");
    }

    @Override
    public void destroy() {
        log.info("mqtt device start destroy....");
        log.info("mqtt device destroy success ....");
    }

}

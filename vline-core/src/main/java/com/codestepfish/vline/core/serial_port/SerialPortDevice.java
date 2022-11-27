package com.codestepfish.vline.core.serial_port;

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
public class SerialPortDevice extends Device {

    @Override
    public void init() {
        log.info("serialPort device start init....");
        log.info("serialPort device init success ....");
    }

    @Override
    public void destroy() {
        log.info("serialPort device start destroy....");
        log.info("serialPort device destroy success ....");
    }

}

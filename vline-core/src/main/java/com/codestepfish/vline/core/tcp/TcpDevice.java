package com.codestepfish.vline.core.tcp;

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
public class TcpDevice extends Device {

    private DeviceType type = DeviceType.TCP;

    private String host;

    private Integer port;

    @Override
    public void init() {
        log.info("tcp device : {} start init....", this.getName());
        log.info("tcp device : {} init success ....", this.getName());
    }

    @Override
    public void destroy() {
        log.info("tcp device : {} start destroy....", this.getName());
        log.info("tcp device : {} destroy success ....", this.getName());

    }
}

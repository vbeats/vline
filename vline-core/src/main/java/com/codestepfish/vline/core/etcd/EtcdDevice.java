package com.codestepfish.vline.core.etcd;

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
public class EtcdDevice extends Device {
    @Override
    public void init() {
        log.info("etcd device start init....");
        log.info("etcd device init success ....");
    }

    @Override
    public void destroy() {
        log.info("etcd device start destroy....");
        log.info("etcd device destroy success ....");
    }
}

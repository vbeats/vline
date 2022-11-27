package com.codestepfish.vline.core.grpc;

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
public class GrpcDevice extends Device {
    @Override
    public void init() {
        log.info("grpc device start init....");
        log.info("grpc device init success ....");
    }

    @Override
    public void destroy() {
        log.info("grpc device start destroy....");
        log.info("grpc device destroy success ....");
    }

}

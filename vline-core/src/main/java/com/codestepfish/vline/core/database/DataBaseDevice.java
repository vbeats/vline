package com.codestepfish.vline.core.database;

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
public class DataBaseDevice extends Device {
    @Override
    public void init() {
        log.info("database device start init....");
        log.info("database device init success ....");
    }

    @Override
    public void destroy() {
        log.info("database device start destroy....");
        log.info("database device destroy success ....");
    }

}

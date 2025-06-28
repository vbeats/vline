package com.codestepfish.vline.core.redis;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
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
public class RedisProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.SINGLE;

    private String dataHandler;

    public enum Mode {
        SINGLE,
        CLUSTER,
        SENTINEL,
        REPLICATED,
        MASTER_SLAVE,
    }
}

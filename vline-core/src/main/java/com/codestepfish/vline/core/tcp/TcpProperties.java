package com.codestepfish.vline.core.tcp;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@Getter
@Setter
@ToString
public class TcpProperties {

    @JSONField(serializeFeatures = JSONWriter.Feature.WriteEnumsUsingName)
    private Mode mode = Mode.SERVER;  // server or client

    private String host = "0.0.0.0";

    private Integer port;

    private Duration reconnectDelay = Duration.ofSeconds(30L);  // client reconnect delay

    private String childHandler;  // ChannelInitializer

    public enum Mode {
        CLIENT,
        SERVER
    }
}

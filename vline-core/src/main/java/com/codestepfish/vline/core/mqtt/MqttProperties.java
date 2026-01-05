package com.codestepfish.vline.core.mqtt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MqttProperties {

    private String broker;

    private ClientVersion clientVersion = ClientVersion.V5;

    private String clientId;

    private String topic;

    private Integer subQos = 0;

    private Integer pubQos = 0;

    private Boolean cleanStart = true;

    private String dataHandler;

    public enum ClientVersion {
        V3,
        V5
    }
}

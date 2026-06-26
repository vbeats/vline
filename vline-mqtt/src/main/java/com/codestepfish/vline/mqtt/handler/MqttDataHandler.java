package com.codestepfish.vline.mqtt.handler;

import com.codestepfish.vline.mqtt.MqttNode;
import org.eclipse.paho.mqttv5.client.MqttCallback;

public interface MqttDataHandler extends MqttCallback {

    // handle mqtt data
    void handle(MqttNode node, Object data);
}

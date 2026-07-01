package com.codestepfish.vline.mqtt.handler;

import com.codestepfish.vline.mqtt.MqttNode;
import org.eclipse.paho.mqttv5.client.MqttCallback;

public interface MqttDataHandler extends MqttCallback {

    void init(MqttNode node);

    void rec(MqttNode node, Object data);
}

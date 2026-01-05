package com.codestepfish.vline.mqtt.handler;

import com.codestepfish.vline.mqtt.MqttNode;
import org.eclipse.paho.mqttv5.client.MqttCallback;

public interface MqttDataHandler extends MqttCallback {
    // publish data to mqtt broker
    <T> void pub(MqttNode mqttNode, T data);
}

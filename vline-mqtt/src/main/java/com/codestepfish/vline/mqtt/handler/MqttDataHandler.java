package com.codestepfish.vline.mqtt.handler;

import com.codestepfish.vline.core.handler.DataHandler;
import com.codestepfish.vline.mqtt.MqttNode;
import org.eclipse.paho.mqttv5.client.MqttCallback;

public interface MqttDataHandler extends DataHandler<MqttNode>, MqttCallback {
}

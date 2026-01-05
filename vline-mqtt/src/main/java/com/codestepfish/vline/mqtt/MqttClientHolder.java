package com.codestepfish.vline.mqtt;

import org.eclipse.paho.mqttv5.client.MqttClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MqttClientHolder {

    // node name -> mqtt client
    public static final Map<String, MqttClient> MQTT_CLIENTS = new ConcurrentHashMap<>(1);
}

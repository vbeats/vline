package com.codestepfish.vlineex.handler;

import com.codestepfish.vline.mqtt.MqttNode;
import com.codestepfish.vline.mqtt.handler.MqttDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttMsgHandler implements MqttDataHandler {


    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {

    }

    @Override
    public void mqttErrorOccurred(MqttException exception) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("messageArrived: {} {}", topic, message);
    }

    @Override
    public void deliveryComplete(IMqttToken token) {

    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {

    }

    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {

    }

    @Override
    public void init(MqttNode node) {

    }

    @Override
    public void rec(MqttNode node, Object data) {

    }

    @Override
    public void destroy(MqttNode node) {

    }
}

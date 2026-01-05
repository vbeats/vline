package com.codestepfish.vlineex.handler;

import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.mqtt.MqttNode;
import com.codestepfish.vline.mqtt.handler.MqttDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

@Slf4j
public class MqttMsgHandler implements MqttDataHandler {

    private final MqttNode mqttNode;

    public MqttMsgHandler(MqttNode mqttNode) {
        this.mqttNode = mqttNode;
    }

    @Override
    public void pub(MqttNode mqttNode, Object data) {
        log.info("pub data: {}", data);
    }

    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {

    }

    @Override
    public void mqttErrorOccurred(MqttException exception) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("messageArrived: {} {}", topic, message);


        VLineContext.posMsg(mqttNode.getName(), message.getPayload());
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
}

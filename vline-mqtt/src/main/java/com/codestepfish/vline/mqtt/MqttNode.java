package com.codestepfish.vline.mqtt;

import cn.hutool.core.util.RandomUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.mqtt.MqttProperties;
import com.codestepfish.vline.mqtt.handler.MqttDataHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class MqttNode extends Node {

    private MqttDataHandler mqttDataHandler;

    @Override
    public void init() {
        super.init();

        try {
            MqttProperties properties = this.getMqtt();

            Assert.hasText(properties.getDataHandler(), "【" + this.getName() + "】 Require Config DataHandler");
            Class<? extends MqttDataHandler> dataHandlerClazz = Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).loadClass(properties.getDataHandler()).asSubclass(MqttDataHandler.class);
            mqttDataHandler = dataHandlerClazz.getDeclaredConstructor(MqttNode.class).newInstance(this);

            // mqtt client 初始化
            // 生成clientId
            String clientId = properties.getClientId() + "_" + RandomUtil.randomNumbers(8);
            MqttClient client = new MqttClient(properties.getBroker(), clientId, new MemoryPersistence());
            MqttConnectionOptions options = new MqttConnectionOptions();

            options.setCleanStart(properties.getCleanStart());
            options.setAutomaticReconnect(true);

            client.connect(options);

            if (client.isConnected()) {
                log.info("【{}】 MQTT Node Init Success... Sub Topic: {}", this.getName(), properties.getTopic());

                client.setCallback(mqttDataHandler);
            }

            client.subscribe(properties.getTopic(), properties.getSubQos());

            MqttClientHolder.MQTT_CLIENTS.put(this.getName(), client);

        } catch (Exception e) {
            log.error("【{}】 Init Failed : ", this.getName(), e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
        super.destroy();

        MqttClientHolder.MQTT_CLIENTS.forEach((K, v) -> {
            try {
                v.disconnect();
                v.close();
            } catch (Exception e) {
                log.error("【{}】 MQTT Node Destroy Failed : ", K, e);
            }
        });
    }

    @Override
    public <T> void receiveData(T data) {
        Thread.ofVirtual().start(() -> mqttDataHandler.pub(this, data));
    }
}

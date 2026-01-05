package com.codestepfish.vline.mqtt;

import cn.hutool.core.bean.BeanUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.core.VLineProperties;
import com.codestepfish.vline.core.enums.NodeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(MqttNode.class)
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class MqttNodeAutoConfiguration implements ApplicationListener {

    private final VLineProperties vLineProperties;

    @ConditionalOnClass(MqttNode.class)
    public void mqttNodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.MQTT.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            MqttNode mqttNode = BeanUtil.copyProperties(node, MqttNode.class);
            mqttNode.init();

            VLineContext.NODES.put(node.getName(), mqttNode);

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            try {
                mqttNodeInit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

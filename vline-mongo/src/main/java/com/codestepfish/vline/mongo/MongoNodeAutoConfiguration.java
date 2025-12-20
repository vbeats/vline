package com.codestepfish.vline.mongo;

import cn.hutool.core.bean.BeanUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.core.VLineProperties;
import com.codestepfish.vline.core.enums.NodeType;
import com.codestepfish.vline.core.mongo.MongoProperties;
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
@ConditionalOnClass(MongoNode.class)
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class MongoNodeAutoConfiguration implements ApplicationListener {

    private final VLineProperties vLineProperties;

    @ConditionalOnClass(MongoNode.class)
    public void mongoNodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.MONGO.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            // mongo init
            MongoNode mongoNode = BeanUtil.copyProperties(node, MongoNode.class);
            mongoNode.init();

            // other mode 不处理消息
            if (!MongoProperties.Mode.OTHER.equals(mongoNode.getMongo().getMode())) {
                VLineContext.NODES.put(node.getName(), mongoNode);
            }

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            try {
                mongoNodeInit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

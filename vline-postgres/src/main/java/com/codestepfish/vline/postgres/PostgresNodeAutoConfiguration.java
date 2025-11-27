package com.codestepfish.vline.postgres;

import cn.hutool.core.bean.BeanUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.core.VLineProperties;
import com.codestepfish.vline.core.enums.NodeType;
import com.codestepfish.vline.core.postgres.PostgresProperties;
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
@ConditionalOnClass(PostgresNode.class)
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class PostgresNodeAutoConfiguration implements ApplicationListener {

    private final VLineProperties vLineProperties;

    @ConditionalOnClass(PostgresNode.class)
    public void postgresNodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.POSTGRES.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            // postgres init
            PostgresNode postgresNode = BeanUtil.copyProperties(node, PostgresNode.class);
            postgresNode.init();

            // other mode 不处理消息
            if (!PostgresProperties.Mode.OTHER.equals(postgresNode.getPostgres().getMode())) {
                VLineContext.NODES.put(node.getName(), postgresNode);
            }

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            try {
                postgresNodeInit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

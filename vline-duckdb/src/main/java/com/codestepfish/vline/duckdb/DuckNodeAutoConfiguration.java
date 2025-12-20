package com.codestepfish.vline.duckdb;

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
@ConditionalOnClass(DuckNode.class)
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class DuckNodeAutoConfiguration implements ApplicationListener {

    private final VLineProperties vLineProperties;

    @ConditionalOnClass(DuckNode.class)
    public void duckNodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.DUCKDB.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            // duck init
            DuckNode duckNode = BeanUtil.copyProperties(node, DuckNode.class);
            duckNode.init();

            VLineContext.NODES.put(node.getName(), duckNode);

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            try {
                duckNodeInit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

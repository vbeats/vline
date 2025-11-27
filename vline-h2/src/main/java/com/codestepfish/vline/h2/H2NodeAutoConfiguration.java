package com.codestepfish.vline.h2;

import cn.hutool.core.bean.BeanUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLineContext;
import com.codestepfish.vline.core.VLineProperties;
import com.codestepfish.vline.core.enums.NodeType;
import com.codestepfish.vline.core.h2.H2Properties;
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
@ConditionalOnClass(H2Node.class)
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class H2NodeAutoConfiguration implements ApplicationListener {

    private final VLineProperties vLineProperties;

    @ConditionalOnClass(H2Node.class)
    public void h2NodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.H2.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            // h2 init
            H2Node h2Node = BeanUtil.copyProperties(node, H2Node.class);
            h2Node.init();

            // other mode 不处理消息
            if (!H2Properties.Mode.OTHER.equals(h2Node.getH2().getMode())) {
                VLineContext.NODES.put(node.getName(), h2Node);
            }

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            try {
                h2NodeInit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

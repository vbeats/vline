package com.codestepfish.vline.spring.boot.starter.etcd;

import cn.hutool.core.bean.BeanUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.NodeType;
import com.codestepfish.vline.core.VLine;
import com.codestepfish.vline.etcd.EtcdNode;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.codestepfish.vline.spring.boot.starter.VLineProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnClass(EtcdNode.class)
@ConditionalOnProperty(prefix = VLine.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline.spring.boot.starter")
public class EtcdNodeAutoConfiguration {

    private final VLineProperties vLineProperties;

    @PostConstruct
    @ConditionalOnClass(EtcdNode.class)
    public void etcdNodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.ETCD.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            EtcdNode etcdNode = BeanUtil.copyProperties(node, EtcdNode.class);
            etcdNode.init();

            VLineContext.NODES.put(node.getName(), etcdNode);
            // event bus
            VLineContext.createEventBus(node.getName());

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }
}

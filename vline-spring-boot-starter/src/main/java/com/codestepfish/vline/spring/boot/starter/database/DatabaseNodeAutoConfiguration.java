package com.codestepfish.vline.spring.boot.starter.database;

import cn.hutool.core.bean.BeanUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.NodeType;
import com.codestepfish.vline.core.VLine;
import com.codestepfish.vline.database.DatabaseNode;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.codestepfish.vline.spring.boot.starter.VLineProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnClass(DatabaseNode.class)
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@ConditionalOnProperty(prefix = VLine.PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline.spring.boot.starter")
public class DatabaseNodeAutoConfiguration {

    private final VLineProperties vLineProperties;

    @PostConstruct
    @ConditionalOnClass(DatabaseNode.class)
    public void databaseNodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.DATABASE.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            DatabaseNode databaseNode = BeanUtil.copyProperties(node, DatabaseNode.class);
            databaseNode.init();

            VLineContext.NODES.put(node.getName(), databaseNode);
            // event bus
            VLineContext.createEventBus(node.getName());

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }
}

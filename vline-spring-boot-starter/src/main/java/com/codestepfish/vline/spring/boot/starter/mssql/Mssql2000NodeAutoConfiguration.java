package com.codestepfish.vline.spring.boot.starter.mssql;

import cn.hutool.core.bean.BeanUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.enums.NodeType;
import com.codestepfish.vline.core.mssql.MssqlProperties;
import com.codestepfish.vline.mssql2000.MssqlNode;
import com.codestepfish.vline.spring.boot.starter.VLineContext;
import com.codestepfish.vline.spring.boot.starter.VLineProperties;
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
@ConditionalOnClass(MssqlNode.class)
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline.spring.boot.starter")
public class Mssql2000NodeAutoConfiguration implements ApplicationListener {

    private final VLineProperties vLineProperties;

    @ConditionalOnClass(MssqlNode.class)
    public void mssqlNodeInit() throws InterruptedException {
        // init node
        List<Node> nodes = vLineProperties.getNodes().stream().filter(e -> NodeType.MSSQL2000.equals(e.getType())).toList();
        CountDownLatch countDownLatch = new CountDownLatch(nodes.size());

        nodes.forEach(node -> {
            // mssql init
            MssqlNode mssqlNode = BeanUtil.copyProperties(node, MssqlNode.class);
            mssqlNode.init();

            // other mode 不处理消息
            if (!MssqlProperties.Mode.OTHER.equals(mssqlNode.getMssql2000().getMode())) {
                VLineContext.NODES.put(node.getName(), mssqlNode);
            }

            countDownLatch.countDown();
        });

        countDownLatch.await();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            try {
                mssqlNodeInit();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

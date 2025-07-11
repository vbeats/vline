package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Node;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline.spring.boot.starter")
public class VLineAutoConfiguration implements InitializingBean, DisposableBean {

    private final VLineProperties vLineProperties;

    @Override
    public void destroy() throws Exception {
        vLineProperties.getNodes().parallelStream().forEach(Node::destroy);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}

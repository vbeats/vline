package com.codestepfish.vline.spring.boot.starter;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLineProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ser.std.SimpleBeanPropertyFilter;
import tools.jackson.databind.ser.std.SimpleFilterProvider;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collection;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class VLineAutoConfiguration implements InitializingBean, DisposableBean {

    private final VLineProperties vLineProperties;
    private final CacheManager cacheManager;

    private final YAMLMapper yamlMapper = getYamlMapper();

    @Override
    public void destroy() throws Exception {
        vLineProperties.getNodes().parallelStream().forEach(Node::destroy);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("****** VLINE CONFIG ****** {}", yamlMapper.writeValueAsString(vLineProperties));

        if (vLineProperties.getCacheStats()) {
            // cache stats
            Thread.ofVirtual().name("VLineCacheStats")
                    .start(() -> {
                        while (true) {
                            ThreadUtil.safeSleep(60000);
                            Collection<String> cacheNames = cacheManager.getCacheNames();

                            cacheNames.forEach(cacheName -> {
                                CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
                                if (caffeineCache != null) {
                                    Cache<Object, Object> cache = caffeineCache.getNativeCache();
                                    CacheStats stats = cache.stats();

                                    log.info("caffeine cache stats : {} ", stats);
                                }
                            });
                        }
                    });
        }
    }

    private YAMLMapper getYamlMapper() {
        return YAMLMapper.builder()
                .addModule(new JavaTimeModule())
                .changeDefaultPropertyInclusion(v -> JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
                .filterProvider(new SimpleFilterProvider().addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept()))
                .build();

    }
}

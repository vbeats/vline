package com.codestepfish.vline.spring.boot.starter;

import com.codestepfish.vline.core.Node;
import com.codestepfish.vline.core.VLineProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import io.netty.util.concurrent.DefaultThreadFactory;
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

import java.util.Collection;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline")
public class VLineAutoConfiguration implements InitializingBean, DisposableBean, TimerTask {

    private final VLineProperties vLineProperties;
    private final CacheManager vlineCacheManager;

    private final YAMLMapper yamlMapper = getYamlMapper();
    private final ThreadFactory threadFactory = new DefaultThreadFactory("cache-stats", true, Thread.NORM_PRIORITY);
    private final HashedWheelTimer timer = new HashedWheelTimer(threadFactory, 100L, TimeUnit.MILLISECONDS, 512, true, 10L);


    @Override
    public void destroy() throws Exception {
        timer.stop();
        vLineProperties.getNodes().parallelStream().forEach(Node::destroy);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("****** VLINE CONFIG ****** {}", yamlMapper.writeValueAsString(vLineProperties));

        if (vLineProperties.getCacheStats()) {
            // cache stats
            timer.newTimeout(this, 0L, TimeUnit.SECONDS);
        }
    }

    private YAMLMapper getYamlMapper() {
        return YAMLMapper.builder()
                .changeDefaultPropertyInclusion(v -> JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
                .filterProvider(new SimpleFilterProvider().addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept()))
                .build();

    }

    @Override
    public void run(Timeout timeout) throws Exception {
        if (timeout.isCancelled()) {
            return;
        }

        Collection<String> cacheNames = vlineCacheManager.getCacheNames();

        cacheNames.forEach(cacheName -> {
            CaffeineCache caffeineCache = (CaffeineCache) vlineCacheManager.getCache(cacheName);
            if (caffeineCache != null) {
                Cache<Object, Object> cache = caffeineCache.getNativeCache();
                CacheStats stats = cache.stats();

                log.info("caffeine cache stats : {} ", stats);
            }
        });

        timer.newTimeout(this, 60L, TimeUnit.SECONDS);
    }
}

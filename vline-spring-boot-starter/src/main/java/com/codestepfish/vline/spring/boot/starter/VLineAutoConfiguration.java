package com.codestepfish.vline.spring.boot.starter;

import cn.hutool.core.thread.ThreadUtil;
import com.codestepfish.vline.core.Node;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({VLineProperties.class})
@ConfigurationPropertiesScan(basePackages = "com.codestepfish.vline.spring.boot.starter")
public class VLineAutoConfiguration implements InitializingBean, DisposableBean {

    private final VLineProperties vLineProperties;
    private final CacheManager cacheManager;

    private final ObjectMapper yamlMapper = createMapper(new YAMLFactory(), null);

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
                                    com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = caffeineCache.getNativeCache();
                                    CacheStats stats = cache.stats();

                                    log.info("caffeine cache stats : {} ", stats);
                                }
                            });
                        }
                    });
        }
    }

    private ObjectMapper createMapper(JsonFactory mapping, ClassLoader classLoader) {
        ObjectMapper mapper = new ObjectMapper(mapping);

        mapper.registerModule(new JavaTimeModule());

        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("classFilter", SimpleBeanPropertyFilter.filterOutAllExcept());
        mapper.setFilterProvider(filterProvider);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        if (classLoader != null) {
            TypeFactory tf = TypeFactory.defaultInstance().withClassLoader(classLoader);
            mapper.setTypeFactory(tf);
        }

        return mapper;
    }
}

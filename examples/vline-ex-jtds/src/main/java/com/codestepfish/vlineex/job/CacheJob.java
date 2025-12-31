package com.codestepfish.vlineex.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheJob {

    private AnylineService cacheService;

    /**
     * cache clean
     */
    @Scheduled(fixedDelay = 5000L)
    public void handler() {

        cacheService = ServiceProxy.service("cache");

        if (ObjectUtils.isEmpty(cacheService)) {
            return;
        }

        log.info("===========> cache clean......");
    }
}

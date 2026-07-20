package org.neverwin.neverwinmockserver.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neverwin.neverwinmockserver.helper.template.TemplateEngineHelper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;
    private final TemplateEngineHelper templateEngineHelper;

    public void clearAllCache() {
        templateEngineHelper.clearCache();
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("all data cache {} removed", cacheName);
            }
        });
    }

    @PostConstruct
    public void init() {
        clearAllCache();
    }

}

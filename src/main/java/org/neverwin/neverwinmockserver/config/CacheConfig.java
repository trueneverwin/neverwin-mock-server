package org.neverwin.neverwinmockserver.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${neverwin.cache.caffeine.expire-minutes:1}")
    private long expireMinutes;

    @Value("${neverwin.cache.caffeine.max-size:1000}")
    private long maxSize;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("scenarioMaster", "scenarioDetails");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(expireMinutes, TimeUnit.MINUTES)
                .maximumSize(maxSize) // Maksimal menyimpan 1000 data di RAM (mencegah OutOfMemory)
                .recordStats()); // Opsional, berguna kalau kamu mau monitoring hit/miss rate

        return cacheManager;
    }

}

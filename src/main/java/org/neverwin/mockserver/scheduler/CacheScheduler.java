package org.neverwin.mockserver.scheduler;

import lombok.RequiredArgsConstructor;
import org.neverwin.mockserver.service.CacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(name = "neverwin.cache.scheduler.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class CacheScheduler {

    private final CacheService cacheService;

    @Value("${neverwin.cache.scheduler.interval-minutes}")
    private String intervalMinutes;

    @Scheduled(
            initialDelayString = "${neverwin.cache.scheduler.interval-minutes}",
            fixedRateString = "${neverwin.cache.scheduler.interval-minutes}",
            timeUnit = TimeUnit.MINUTES
    )
    public void evictAllCachesAtIntervals() {
        cacheService.clearAllCache();
        log.info("Semua cache berhasil di-evict berdasarkan jadwal ({} menit).",
                intervalMinutes);
    }
}
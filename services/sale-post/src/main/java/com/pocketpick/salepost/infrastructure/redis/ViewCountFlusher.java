package com.pocketpick.salepost.infrastructure.redis;

import com.pocketpick.salepost.domain.service.SalePostUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountFlusher {

    private final ViewCountRepository viewCountRepository;
    private final SalePostUseCase salePostUseCase;

    @Scheduled(fixedDelay = 60_000)
    public void flush() {
        Set<String> keys = viewCountRepository.getAllKeys();
        if (keys == null || keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            Long salePostId = viewCountRepository.extractSalePostId(key);
            Long delta = viewCountRepository.getAndDelete(salePostId);
            if (delta <= 0) {
                continue;
            }
            try {
                salePostUseCase.applyViewCount(salePostId, delta.intValue());
                log.debug("viewCount flushed: salePostId={}, delta={}", salePostId, delta);
            } catch (Exception e) {
                log.error("viewCount flush 실패, Redis 복구 시도: salePostId={}, delta={}", salePostId, delta, e);
                try {
                    viewCountRepository.increment(salePostId, delta);
                } catch (Exception redisEx) {
                    log.error("Redis 보상 실패, 조회수 유실 발생: salePostId={}, delta={}", salePostId, delta, redisEx);
                }
            }
        }
    }
}

package com.pocketpick.salepost.infrastructure.redis;

import com.pocketpick.salepost.infrastructure.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountFlusher {

    private final ViewCountRepository viewCountRepository;
    private final SalePostRepository salePostRepository;

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void flush() {
        Set<String> keys = viewCountRepository.getAllKeys();
        if (keys == null || keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            Long salePostId = viewCountRepository.extractSalePostId(key);
            Long delta = viewCountRepository.get(salePostId);
            if (delta != null && delta > 0) {
                salePostRepository.incrementViewCount(salePostId, delta.intValue());
                viewCountRepository.delete(salePostId);
                log.debug("viewCount flushed: salePostId={}, delta={}", salePostId, delta);
            }
        }
    }
}

package com.pocketpick.user.infrastructure.redis;

import com.pocketpick.user.domain.event.FcmTokenRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FcmTokenEventListener {

    private final FcmTokenRedisRepository fcmTokenRedisRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(FcmTokenRegisteredEvent event) {
        fcmTokenRedisRepository.save(event.userId(), event.fcmToken());
    }
}

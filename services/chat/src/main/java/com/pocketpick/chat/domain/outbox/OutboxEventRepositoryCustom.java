package com.pocketpick.chat.domain.outbox;

import java.util.Optional;

public interface OutboxEventRepositoryCustom {

    Optional<OutboxEvent> findAndMarkProcessing();
}

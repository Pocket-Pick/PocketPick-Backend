package com.pocketpick.chat.infrastructure.kafka;

import com.pocketpick.chat.domain.message.dto.ChatMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ChatMessageEventListener {

    private final ChatMessageProducer chatMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatMessageEvent event) {
        chatMessageProducer.send(event);
    }
}

package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.message.ChatMessage;
import com.pocketpick.chat.domain.message.ChatMessageRepository;
import com.pocketpick.chat.domain.message.dto.MessageHistoryResponse;
import com.pocketpick.chat.domain.message.dto.MessageHistoryResponse.MessageItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageHistoryService {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ChatMessageRepository chatMessageRepository;

    public MessageHistoryResponse getHistory(String roomId, String cursor, int size) {
        int fetchSize = size <= 0 ? DEFAULT_PAGE_SIZE : size;
        PageRequest pageable = PageRequest.of(0, fetchSize + 1);

        List<ChatMessage> messages = cursor == null
                ? chatMessageRepository.findByRoomIdOrderByIdDesc(roomId, pageable)
                : chatMessageRepository.findByRoomIdAndIdLessThanOrderByIdDesc(roomId, cursor, pageable);

        boolean hasNext = messages.size() > fetchSize;
        List<ChatMessage> page = hasNext ? messages.subList(0, fetchSize) : messages;

        String nextCursor = hasNext ? page.get(page.size() - 1).getId() : null;

        List<MessageItem> items = page.stream()
                .map(MessageItem::from)
                .toList();

        return new MessageHistoryResponse(items, nextCursor, hasNext);
    }
}

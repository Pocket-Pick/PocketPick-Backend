package com.pocketpick.chat.presentation.rest;

import com.pocketpick.chat.application.MessageHistoryService;
import com.pocketpick.chat.domain.message.dto.MessageHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/rooms")
@RequiredArgsConstructor
public class MessageController {

    private final MessageHistoryService messageHistoryService;

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<MessageHistoryResponse> getMessages(
            @PathVariable String roomId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(messageHistoryService.getHistory(roomId, cursor, size));
    }
}

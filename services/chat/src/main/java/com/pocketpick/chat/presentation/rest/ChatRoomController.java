package com.pocketpick.chat.presentation.rest;

import com.pocketpick.chat.domain.room.ChatRoomUseCase;
import com.pocketpick.chat.domain.room.dto.ChatRoomResponse;
import com.pocketpick.chat.domain.room.dto.CreateChatRoomRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomUseCase chatRoomUseCase;

    @PostMapping
    public ResponseEntity<ChatRoomResponse> createOrGet(@Valid @RequestBody CreateChatRoomRequest request) {
        return ResponseEntity.ok(chatRoomUseCase.createOrGet(request));
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> listRooms(@RequestParam Long userId) {
        return ResponseEntity.ok(chatRoomUseCase.listRooms(userId));
    }
}

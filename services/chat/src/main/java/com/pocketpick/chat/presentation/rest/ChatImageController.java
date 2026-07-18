package com.pocketpick.chat.presentation.rest;

import com.pocketpick.chat.domain.image.ChatImageUseCase;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlRequest;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/image")
@RequiredArgsConstructor
public class ChatImageController {

    private final ChatImageUseCase chatImageUseCase;

    @PostMapping("/presigned-url")
    public ResponseEntity<ChatImagePresignedUrlResponse> generatePresignedUrl(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ChatImagePresignedUrlRequest request
    ) {
        return ResponseEntity.ok(chatImageUseCase.generatePresignedUrl(userId, request));
    }
}

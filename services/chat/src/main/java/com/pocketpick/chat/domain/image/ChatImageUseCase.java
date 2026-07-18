package com.pocketpick.chat.domain.image;

import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlRequest;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlResponse;

public interface ChatImageUseCase {

    ChatImagePresignedUrlResponse generatePresignedUrl(Long userId, ChatImagePresignedUrlRequest request);
}

package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.image.ChatImageUseCase;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlRequest;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlResponse;
import com.pocketpick.chat.infrastructure.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatImageService implements ChatImageUseCase {

    private final S3Uploader s3Uploader;

    @Override
    public ChatImagePresignedUrlResponse generatePresignedUrl(Long userId, ChatImagePresignedUrlRequest request) {
        String objectKey = s3Uploader.generateChatObjectKey(userId, request.extension());
        String presignedUrl = s3Uploader.generatePresignedUrl(objectKey);
        String imageUrl = s3Uploader.buildImageUrl(objectKey);
        return new ChatImagePresignedUrlResponse(presignedUrl, imageUrl);
    }
}

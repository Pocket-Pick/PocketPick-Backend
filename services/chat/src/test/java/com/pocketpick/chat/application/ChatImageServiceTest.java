package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlRequest;
import com.pocketpick.chat.domain.image.dto.ChatImagePresignedUrlResponse;
import com.pocketpick.chat.infrastructure.s3.S3Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("ChatImageService")
@ExtendWith(MockitoExtension.class)
class ChatImageServiceTest {

    @Mock private S3Uploader s3Uploader;

    @InjectMocks
    private ChatImageService chatImageService;

    @Nested
    @DisplayName("presigned URL 발급")
    class GeneratePresignedUrl {

        @Test
        @DisplayName("유효한 요청이면 presignedUrl과 imageUrl을 반환한다")
        void generatePresignedUrl_validRequest_returnsPresignedUrlAndImageUrl() {
            Long userId = 1L;
            ChatImagePresignedUrlRequest request = new ChatImagePresignedUrlRequest("jpg");
            String objectKey = "images/chat/1/uuid.jpg";
            String presignedUrl = "https://s3.amazonaws.com/bucket/images/chat/1/uuid.jpg?X-Amz-Signature=abc";
            String imageUrl = "https://bucket.s3.amazonaws.com/images/chat/1/uuid.jpg";

            given(s3Uploader.generateChatObjectKey(userId, "jpg")).willReturn(objectKey);
            given(s3Uploader.generatePresignedUrl(objectKey)).willReturn(presignedUrl);
            given(s3Uploader.buildImageUrl(objectKey)).willReturn(imageUrl);

            ChatImagePresignedUrlResponse response = chatImageService.generatePresignedUrl(userId, request);

            assertThat(response.presignedUrl()).isEqualTo(presignedUrl);
            assertThat(response.imageUrl()).isEqualTo(imageUrl);
            verify(s3Uploader).generateChatObjectKey(userId, "jpg");
            verify(s3Uploader).generatePresignedUrl(objectKey);
            verify(s3Uploader).buildImageUrl(objectKey);
        }
    }
}

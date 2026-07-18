package com.pocketpick.chat.infrastructure.s3;

import com.pocketpick.chat.global.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofMinutes(10);
    private static final String CHAT_PREFIX = "images/chat/";

    private final S3Presigner s3Presigner;
    private final AwsProperties awsProperties;

    public String generateChatObjectKey(Long userId, String extension) {
        return CHAT_PREFIX + userId + "/" + UUID.randomUUID() + "." + extension;
    }

    public String generatePresignedUrl(String objectKey) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(objectKey)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(PRESIGNED_URL_EXPIRATION)
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    public String buildImageUrl(String objectKey) {
        return "https://" + awsProperties.getS3().getBucket() + ".s3.amazonaws.com/" + objectKey;
    }
}

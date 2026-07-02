package com.pocketpick.salepost.infrastructure.s3;

import com.pocketpick.salepost.global.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
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
    private static final String TEMP_PREFIX = "images/temp/";
    private static final String POSTS_PREFIX = "images/posts/";

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AwsProperties awsProperties;

    public String generateTempObjectKey(Long userId, String extension) {
        return TEMP_PREFIX + userId + "/" + UUID.randomUUID() + "." + extension;
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

    /**
     * temp 경로의 객체를 posts 경로로 복사하고 새로운 objectKey를 반환한다.
     * temp 원본은 S3 Lifecycle 정책으로 7일 후 자동 삭제된다.
     */
    public String promoteToPostsPath(String tempObjectKey) {
        String filename = tempObjectKey.substring(tempObjectKey.lastIndexOf('/') + 1);
        String userId = tempObjectKey.split("/")[2];
        String destKey = POSTS_PREFIX + userId + "/" + filename;

        String bucket = awsProperties.getS3().getBucket();
        s3Client.copyObject(CopyObjectRequest.builder()
                .sourceBucket(bucket)
                .sourceKey(tempObjectKey)
                .destinationBucket(bucket)
                .destinationKey(destKey)
                .build());

        return destKey;
    }

    public void deleteObject(String objectKey) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(objectKey)
                .build());
    }

    public String buildImageUrl(String objectKey) {
        return "https://" + awsProperties.getS3().getBucket() + ".s3.amazonaws.com/" + objectKey;
    }
}

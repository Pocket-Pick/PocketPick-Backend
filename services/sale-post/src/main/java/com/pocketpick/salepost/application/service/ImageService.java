package com.pocketpick.salepost.application.service;

import com.pocketpick.salepost.application.dto.PresignedUrlRequest;
import com.pocketpick.salepost.application.dto.PresignedUrlResponse;
import com.pocketpick.salepost.application.port.ImageUseCase;
import com.pocketpick.salepost.infrastructure.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageUseCase {

    private static final String IMAGE_PATH_PREFIX = "images/";

    private final S3Uploader s3Uploader;

    @Override
    public PresignedUrlResponse generatePresignedUrl(Long userId, PresignedUrlRequest request) {
        String extension = extractExtension(request.filename());
        String objectKey = IMAGE_PATH_PREFIX + userId + "/" + UUID.randomUUID() + "." + extension;
        String presignedUrl = s3Uploader.generatePresignedUrl(objectKey);
        return new PresignedUrlResponse(presignedUrl, objectKey);
    }

    private String extractExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}

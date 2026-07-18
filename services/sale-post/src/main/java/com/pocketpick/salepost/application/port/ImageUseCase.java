package com.pocketpick.salepost.application.port;

import com.pocketpick.salepost.application.dto.PresignedUrlRequest;
import com.pocketpick.salepost.application.dto.PresignedUrlResponse;

public interface ImageUseCase {

    PresignedUrlResponse generatePresignedUrl(Long userId, PresignedUrlRequest request);
}

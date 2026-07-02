package com.pocketpick.salepost.domain.service;

import com.pocketpick.salepost.domain.dto.PresignedUrlRequest;
import com.pocketpick.salepost.domain.dto.PresignedUrlResponse;

public interface ImageUseCase {

    PresignedUrlResponse generatePresignedUrl(Long userId, PresignedUrlRequest request);
}

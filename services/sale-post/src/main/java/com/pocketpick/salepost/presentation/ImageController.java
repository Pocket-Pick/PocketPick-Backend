package com.pocketpick.salepost.presentation;

import com.pocketpick.salepost.application.dto.PresignedUrlRequest;
import com.pocketpick.salepost.application.dto.PresignedUrlResponse;
import com.pocketpick.salepost.application.port.ImageUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUseCase imageUseCase;

    @PostMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid PresignedUrlRequest request
    ) {
        return ResponseEntity.ok(imageUseCase.generatePresignedUrl(userId, request));
    }
}

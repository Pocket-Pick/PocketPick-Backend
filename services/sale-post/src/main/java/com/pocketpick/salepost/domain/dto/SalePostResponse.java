package com.pocketpick.salepost.domain.dto;

import com.pocketpick.salepost.domain.domain.SalePost;
import com.pocketpick.salepost.domain.domain.SaleStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SalePostResponse(
        Long id,
        Long userId,
        String title,
        String description,
        int price,
        SaleStatus status,
        List<SalePostItemResponse> items,
        List<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static SalePostResponse from(SalePost salePost, List<SalePostItemResponse> items, List<String> imageUrls) {
        return new SalePostResponse(
                salePost.getId(),
                salePost.getUserId(),
                salePost.getTitle(),
                salePost.getDescription(),
                salePost.getPrice(),
                salePost.getStatus(),
                items,
                imageUrls,
                salePost.getCreatedAt(),
                salePost.getUpdatedAt()
        );
    }
}

package com.pocketpick.salepost.domain.dto;

import com.pocketpick.salepost.domain.domain.CardCondition;
import com.pocketpick.salepost.domain.domain.SalePost;
import com.pocketpick.salepost.domain.domain.SaleStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SalePostResponse(
        Long id,
        Long userId,
        Long cardId,
        String title,
        String description,
        int price,
        CardCondition cardCondition,
        SaleStatus status,
        List<String> imageUrls,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static SalePostResponse from(SalePost salePost, List<String> imageUrls) {
        return new SalePostResponse(
                salePost.getId(),
                salePost.getUserId(),
                salePost.getCardId(),
                salePost.getTitle(),
                salePost.getDescription(),
                salePost.getPrice(),
                salePost.getCardCondition(),
                salePost.getStatus(),
                imageUrls,
                salePost.getCreatedAt(),
                salePost.getUpdatedAt()
        );
    }
}

package com.pocketpick.salepost.application.dto;

import com.pocketpick.salepost.domain.entity.CardCondition;
import com.pocketpick.salepost.domain.entity.SalePost;
import com.pocketpick.salepost.domain.entity.SaleStatus;

import java.time.LocalDateTime;

public record SalePostResponse(
        Long id,
        Long userId,
        Long cardId,
        String title,
        String description,
        int price,
        CardCondition cardCondition,
        SaleStatus status,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static SalePostResponse from(SalePost salePost, String imageUrl) {
        return new SalePostResponse(
                salePost.getId(),
                salePost.getUserId(),
                salePost.getCardId(),
                salePost.getTitle(),
                salePost.getDescription(),
                salePost.getPrice(),
                salePost.getCardCondition(),
                salePost.getStatus(),
                imageUrl,
                salePost.getCreatedAt(),
                salePost.getUpdatedAt()
        );
    }
}

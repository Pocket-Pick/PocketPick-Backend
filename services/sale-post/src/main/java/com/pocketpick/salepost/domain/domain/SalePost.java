package com.pocketpick.salepost.domain.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sale_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long cardId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CardCondition cardCondition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SaleStatus status;

    private String imageObjectKey;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private SalePost(Long userId, Long cardId, String title, String description,
                     int price, CardCondition cardCondition, String imageObjectKey) {
        this.userId = userId;
        this.cardId = cardId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.cardCondition = cardCondition;
        this.imageObjectKey = imageObjectKey;
        this.status = SaleStatus.ON_SALE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String description, int price,
                       CardCondition cardCondition, String imageObjectKey) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.cardCondition = cardCondition;
        if (imageObjectKey != null) {
            this.imageObjectKey = imageObjectKey;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(SaleStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}

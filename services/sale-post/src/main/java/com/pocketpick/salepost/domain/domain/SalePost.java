package com.pocketpick.salepost.domain.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "sale_post")
@EntityListeners(AuditingEntityListener.class)
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

    @Column(length = 500)
    private String imageObjectKey;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private SalePost(Long userId, Long cardId, String title, String description,
                     int price, CardCondition cardCondition, String imageObjectKey) {
        if (price < 0) throw new IllegalArgumentException("price는 0 이상이어야 합니다.");
        this.userId = userId;
        this.cardId = cardId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.cardCondition = cardCondition;
        this.imageObjectKey = imageObjectKey;
        this.status = SaleStatus.ON_SALE;
    }

    public void update(String title, String description, int price,
                       CardCondition cardCondition, String imageObjectKey) {
        if (price < 0) throw new IllegalArgumentException("price는 0 이상이어야 합니다.");
        this.title = title;
        this.description = description;
        this.price = price;
        this.cardCondition = cardCondition;
        if (imageObjectKey != null) {
            this.imageObjectKey = imageObjectKey;
        }
    }

    public void updateStatus(SaleStatus status) {
        this.status = status;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}

package com.pocketpick.salepost.domain.domain;

import com.pocketpick.salepost.domain.domain.exception.InvalidStatusTransitionException;
import com.pocketpick.salepost.domain.domain.exception.ReservedPostException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@SoftDelete
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

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SaleStatus status;

    @Column(length = 500)
    private String imageObjectKey;

    @Column(nullable = false)
    private int viewCount;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private SalePost(Long userId, String title, String description, int price, String imageObjectKey) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageObjectKey = imageObjectKey;
        this.status = SaleStatus.ON_SALE;
        this.viewCount = 0;
    }

    public void update(String title, String description, Integer price, String imageObjectKey) {
        if (this.status == SaleStatus.RESERVED) {
            throw new ReservedPostException();
        }
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (price != null) {
            if (price < 0) throw new IllegalArgumentException("price는 0 이상이어야 합니다.");
            this.price = price;
        }
        if (imageObjectKey != null) this.imageObjectKey = imageObjectKey;
        this.updatedAt = LocalDateTime.now();
    }

    public void validateDeletable() {
        if (this.status == SaleStatus.RESERVED) {
            throw new ReservedPostException();
        }
    }

    public void updateStatus(SaleStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new InvalidStatusTransitionException();
        }
        this.status = newStatus;
    }

    public void incrementViewCount(int delta) {
        this.viewCount += delta;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }
}

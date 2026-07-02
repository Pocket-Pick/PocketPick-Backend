package com.pocketpick.salepost.domain.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sale_post_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalePostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long salePostId;

    @Column(nullable = false, length = 500)
    private String objectKey;

    @Column(nullable = false)
    private int sortOrder;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static SalePostImage of(Long salePostId, String objectKey, int sortOrder) {
        SalePostImage image = new SalePostImage();
        image.salePostId = salePostId;
        image.objectKey = objectKey;
        image.sortOrder = sortOrder;
        image.createdAt = LocalDateTime.now();
        return image;
    }
}

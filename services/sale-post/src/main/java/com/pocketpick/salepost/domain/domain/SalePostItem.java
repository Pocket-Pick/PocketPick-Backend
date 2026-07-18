package com.pocketpick.salepost.domain.domain;

import com.pocketpick.salepost.domain.domain.exception.InvalidItemQuantityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale_post_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalePostItem {

    private static final int MIN_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long salePostId;

    @Column(nullable = false)
    private Long cardId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CardCondition cardCondition;

    @Column(nullable = false)
    private int quantity;

    public static SalePostItem of(Long salePostId, Long cardId, CardCondition cardCondition, int quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new InvalidItemQuantityException();
        }
        SalePostItem item = new SalePostItem();
        item.salePostId = salePostId;
        item.cardId = cardId;
        item.cardCondition = cardCondition;
        item.quantity = quantity;
        return item;
    }
}

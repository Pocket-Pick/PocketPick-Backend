package com.pocketpick.salepost.domain.dto;

import com.pocketpick.salepost.domain.domain.CardCondition;
import com.pocketpick.salepost.domain.domain.SalePostItem;

public record SalePostItemResponse(
        Long cardId,
        CardCondition cardCondition,
        int quantity
) {

    public static SalePostItemResponse from(SalePostItem item) {
        return new SalePostItemResponse(item.getCardId(), item.getCardCondition(), item.getQuantity());
    }
}

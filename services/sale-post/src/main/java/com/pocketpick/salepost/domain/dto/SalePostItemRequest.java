package com.pocketpick.salepost.domain.dto;

import com.pocketpick.salepost.domain.domain.CardCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SalePostItemRequest(
        @NotNull
        Long cardId,

        @NotNull
        CardCondition cardCondition,

        @Min(1)
        int quantity
) {
}

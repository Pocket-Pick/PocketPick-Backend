package com.pocketpick.salepost.application.dto;

import com.pocketpick.salepost.domain.entity.CardCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateSalePostRequest(
        @Size(max = 100)
        String title,

        @Size(max = 2000)
        String description,

        @Min(0)
        Integer price,

        CardCondition cardCondition,

        String imageObjectKey
) {
}

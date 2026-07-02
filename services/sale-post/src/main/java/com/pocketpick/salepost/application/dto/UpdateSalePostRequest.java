package com.pocketpick.salepost.application.dto;

import com.pocketpick.salepost.domain.entity.CardCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateSalePostRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        String description,

        @Min(0)
        int price,

        @NotNull
        CardCondition cardCondition,

        String imageObjectKey
) {
}

package com.pocketpick.salepost.domain.dto;

import com.pocketpick.salepost.domain.domain.CardCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateSalePostRequest(
        @NotNull
        Long cardId,

        @NotBlank
        @Size(max = 100)
        String title,

        String description,

        @Min(0)
        int price,

        @NotNull
        CardCondition cardCondition,

        List<String> imageObjectKeys
) {
}

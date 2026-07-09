package com.pocketpick.salepost.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateSalePostRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        String description,

        @Min(0)
        int price,

        @NotEmpty
        @Valid
        List<SalePostItemRequest> items,

        List<String> imageObjectKeys
) {
}

package com.pocketpick.salepost.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateSalePostRequest(
        @Size(max = 100)
        String title,

        @Size(max = 2000)
        String description,

        @Min(0)
        Integer price,

        @NotEmpty
        @Valid
        List<SalePostItemRequest> items,

        List<String> imageObjectKeys
) {
}

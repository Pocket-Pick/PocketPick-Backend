package com.pocketpick.salepost.domain.dto;

import com.pocketpick.salepost.domain.domain.SaleStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateSaleStatusRequest(
        @NotNull
        SaleStatus status
) {
}

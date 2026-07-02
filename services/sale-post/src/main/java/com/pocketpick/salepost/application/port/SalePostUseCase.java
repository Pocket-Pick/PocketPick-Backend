package com.pocketpick.salepost.application.port;

import com.pocketpick.salepost.application.dto.CreateSalePostRequest;
import com.pocketpick.salepost.application.dto.SalePostResponse;
import com.pocketpick.salepost.application.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.domain.entity.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalePostUseCase {

    SalePostResponse create(Long userId, CreateSalePostRequest request);

    Page<SalePostResponse> getList(Long cardId, SaleStatus status, Pageable pageable);

    SalePostResponse getOne(Long id);

    SalePostResponse update(Long userId, Long id, UpdateSalePostRequest request);

    void delete(Long userId, Long id);
}

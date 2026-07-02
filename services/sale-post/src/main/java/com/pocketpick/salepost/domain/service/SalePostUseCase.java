package com.pocketpick.salepost.domain.service;

import com.pocketpick.salepost.domain.dto.CreateSalePostRequest;
import com.pocketpick.salepost.domain.dto.SalePostResponse;
import com.pocketpick.salepost.domain.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.domain.dto.UpdateSaleStatusRequest;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalePostUseCase {

    SalePostResponse create(Long userId, CreateSalePostRequest request);

    Page<SalePostResponse> getSalePostList(Long cardId, SaleStatus status, Pageable pageable);

    SalePostResponse getSalePost(Long id);

    SalePostResponse update(Long userId, Long id, UpdateSalePostRequest request);

    SalePostResponse updateStatus(Long userId, Long id, UpdateSaleStatusRequest request);

    void delete(Long userId, Long id);
}

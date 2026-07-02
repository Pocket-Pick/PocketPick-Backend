package com.pocketpick.salepost.domain.controller;

import com.pocketpick.salepost.domain.dto.CreateSalePostRequest;
import com.pocketpick.salepost.domain.dto.SalePostResponse;
import com.pocketpick.salepost.domain.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.domain.dto.UpdateSaleStatusRequest;
import com.pocketpick.salepost.domain.service.SalePostUseCase;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/sale-posts")
@RequiredArgsConstructor
public class SalePostController {

    private final SalePostUseCase salePostUseCase;

    @PostMapping
    public ResponseEntity<SalePostResponse> create(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid CreateSalePostRequest request
    ) {
        SalePostResponse response = salePostUseCase.create(userId, request);
        return ResponseEntity.created(URI.create("/sale-posts/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<SalePostResponse>> getList(
            @RequestParam(required = false) Long cardId,
            @RequestParam(required = false) SaleStatus status,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(salePostUseCase.getList(cardId, status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalePostResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(salePostUseCase.getOne(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SalePostResponse> update(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody @Valid UpdateSalePostRequest request
    ) {
        return ResponseEntity.ok(salePostUseCase.update(userId, id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SalePostResponse> updateStatus(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody @Valid UpdateSaleStatusRequest request
    ) {
        return ResponseEntity.ok(salePostUseCase.updateStatus(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id
    ) {
        salePostUseCase.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}

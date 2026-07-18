package com.pocketpick.salepost.application.service;

import com.pocketpick.salepost.application.dto.CreateSalePostRequest;
import com.pocketpick.salepost.application.dto.SalePostResponse;
import com.pocketpick.salepost.application.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.application.port.SalePostUseCase;
import com.pocketpick.salepost.domain.entity.SalePost;
import com.pocketpick.salepost.domain.entity.SaleStatus;
import com.pocketpick.salepost.domain.exception.ForbiddenException;
import com.pocketpick.salepost.domain.exception.SalePostNotFoundException;
import com.pocketpick.salepost.infrastructure.repository.SalePostRepository;
import com.pocketpick.salepost.infrastructure.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalePostService implements SalePostUseCase {

    private final SalePostRepository salePostRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public SalePostResponse create(Long userId, CreateSalePostRequest request) {
        SalePost salePost = SalePost.builder()
                .userId(userId)
                .cardId(request.cardId())
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .cardCondition(request.cardCondition())
                .imageObjectKey(request.imageObjectKey())
                .build();
        return toResponse(salePostRepository.save(salePost));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalePostResponse> getList(Long cardId, SaleStatus status, Pageable pageable) {
        Page<SalePost> posts;
        if (cardId != null && status != null) {
            posts = salePostRepository.findByCardIdAndStatus(cardId, status, pageable);
        } else if (cardId != null) {
            posts = salePostRepository.findByCardId(cardId, pageable);
        } else if (status != null) {
            posts = salePostRepository.findByStatus(status, pageable);
        } else {
            posts = salePostRepository.findAll(pageable);
        }
        return posts.map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SalePostResponse getOne(Long id) {
        SalePost salePost = salePostRepository.findById(id)
                .orElseThrow(SalePostNotFoundException::new);
        return toResponse(salePost);
    }

    @Override
    @Transactional
    public SalePostResponse update(Long userId, Long id, UpdateSalePostRequest request) {
        SalePost salePost = salePostRepository.findById(id)
                .orElseThrow(SalePostNotFoundException::new);
        if (!salePost.isOwner(userId)) {
            throw new ForbiddenException();
        }
        salePost.update(request.title(), request.description(), request.price(),
                request.cardCondition(), request.imageObjectKey());
        return toResponse(salePost);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long id) {
        SalePost salePost = salePostRepository.findById(id)
                .orElseThrow(SalePostNotFoundException::new);
        if (!salePost.isOwner(userId)) {
            throw new ForbiddenException();
        }
        salePostRepository.delete(salePost);
    }

    private SalePostResponse toResponse(SalePost salePost) {
        String imageUrl = salePost.getImageObjectKey() != null
                ? s3Uploader.buildImageUrl(salePost.getImageObjectKey())
                : null;
        return SalePostResponse.from(salePost, imageUrl);
    }
}

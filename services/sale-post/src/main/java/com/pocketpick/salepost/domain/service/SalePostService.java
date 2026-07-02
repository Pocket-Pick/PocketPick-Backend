package com.pocketpick.salepost.domain.service;

import com.pocketpick.salepost.domain.dto.CreateSalePostRequest;
import com.pocketpick.salepost.domain.dto.SalePostResponse;
import com.pocketpick.salepost.domain.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.domain.domain.SalePost;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import com.pocketpick.salepost.domain.domain.exception.ForbiddenException;
import com.pocketpick.salepost.domain.domain.exception.SalePostNotFoundException;
import com.pocketpick.salepost.infrastructure.repository.SalePostImageRepository;
import com.pocketpick.salepost.infrastructure.repository.SalePostRepository;
import com.pocketpick.salepost.infrastructure.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalePostService implements SalePostUseCase {

    private final SalePostRepository salePostRepository;
    private final SalePostImageRepository salePostImageRepository;
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
        return posts.map(post -> toResponse(post));
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
                request.cardCondition());
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
        List<String> imageUrls = salePostImageRepository.findBySalePostIdOrderBySortOrder(salePost.getId())
                .stream()
                .map(image -> s3Uploader.buildImageUrl(image.getObjectKey()))
                .toList();
        return SalePostResponse.from(salePost, imageUrls);
    }
}

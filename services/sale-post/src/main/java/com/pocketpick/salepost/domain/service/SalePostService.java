package com.pocketpick.salepost.domain.service;

import com.pocketpick.salepost.domain.domain.SalePostImage;
import com.pocketpick.salepost.domain.dto.CreateSalePostRequest;
import com.pocketpick.salepost.domain.dto.SalePostResponse;
import com.pocketpick.salepost.domain.dto.UpdateSalePostRequest;
import com.pocketpick.salepost.domain.dto.UpdateSaleStatusRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        SalePost saved = salePostRepository.save(salePost);

        saveImages(saved.getId(), request.imageObjectKeys());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalePostResponse> getSalePostList(Long cardId, SaleStatus status, Pageable pageable) {
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
    public SalePostResponse getSalePost(Long id) {
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

        salePostImageRepository.deleteBySalePostId(id);
        saveImages(id, request.imageObjectKeys());

        return toResponse(salePost);
    }

    @Override
    @Transactional
    public SalePostResponse updateStatus(Long userId, Long id, UpdateSaleStatusRequest request) {
        SalePost salePost = salePostRepository.findById(id)
                .orElseThrow(SalePostNotFoundException::new);
        if (!salePost.isOwner(userId)) {
            throw new ForbiddenException();
        }
        salePost.updateStatus(request.status());

        if (request.status() == SaleStatus.SOLD) {
            List<SalePostImage> images = salePostImageRepository.findBySalePostIdOrderBySortOrder(id);
            images.forEach(image -> s3Uploader.deleteObject(image.getObjectKey()));
            salePostImageRepository.deleteBySalePostId(id);
        }

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
        List<SalePostImage> images = salePostImageRepository.findBySalePostIdOrderBySortOrder(id);
        images.forEach(image -> s3Uploader.deleteObject(image.getObjectKey()));
        salePostImageRepository.deleteBySalePostId(id);
        salePostRepository.delete(salePost);
    }

    private void saveImages(Long salePostId, List<String> tempObjectKeys) {
        if (tempObjectKeys == null || tempObjectKeys.isEmpty()) {
            return;
        }
        List<SalePostImage> images = new ArrayList<>();
        for (int i = 0; i < tempObjectKeys.size(); i++) {
            String postsKey = s3Uploader.promoteToPostsPath(tempObjectKeys.get(i));
            images.add(SalePostImage.of(salePostId, postsKey, i));
        }
        salePostImageRepository.saveAll(images);
    }

    private SalePostResponse toResponse(SalePost salePost) {
        List<String> imageUrls = salePostImageRepository.findBySalePostIdOrderBySortOrder(salePost.getId())
                .stream()
                .map(image -> s3Uploader.buildImageUrl(image.getObjectKey()))
                .toList();
        return SalePostResponse.from(salePost, imageUrls);
    }
}

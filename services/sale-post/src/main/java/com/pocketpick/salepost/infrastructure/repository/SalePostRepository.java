package com.pocketpick.salepost.infrastructure.repository;

import com.pocketpick.salepost.domain.domain.SalePost;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalePostRepository extends JpaRepository<SalePost, Long> {

    Page<SalePost> findByCardIdAndStatus(Long cardId, SaleStatus status, Pageable pageable);

    Page<SalePost> findByCardId(Long cardId, Pageable pageable);

    Page<SalePost> findByStatus(SaleStatus status, Pageable pageable);
}

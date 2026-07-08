package com.pocketpick.salepost.infrastructure.repository;

import com.pocketpick.salepost.domain.entity.SalePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SalePostRepository extends JpaRepository<SalePost, Long>,
        JpaSpecificationExecutor<SalePost> {
}

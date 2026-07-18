package com.pocketpick.salepost.infrastructure.repository;

import com.pocketpick.salepost.domain.domain.SalePostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalePostImageRepository extends JpaRepository<SalePostImage, Long> {

    List<SalePostImage> findBySalePostIdOrderBySortOrder(Long salePostId);

    void deleteBySalePostId(Long salePostId);
}

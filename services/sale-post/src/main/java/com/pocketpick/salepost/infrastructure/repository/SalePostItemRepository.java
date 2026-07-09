package com.pocketpick.salepost.infrastructure.repository;

import com.pocketpick.salepost.domain.domain.SalePostItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalePostItemRepository extends JpaRepository<SalePostItem, Long> {

    List<SalePostItem> findBySalePostId(Long salePostId);

    void deleteBySalePostId(Long salePostId);
}

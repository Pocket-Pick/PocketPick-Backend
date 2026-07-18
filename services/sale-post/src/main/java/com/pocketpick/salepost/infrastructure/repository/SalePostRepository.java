package com.pocketpick.salepost.infrastructure.repository;

import com.pocketpick.salepost.domain.domain.SalePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SalePostRepository extends JpaRepository<SalePost, Long>,
        JpaSpecificationExecutor<SalePost> {

    @Modifying
    @Query("UPDATE SalePost s SET s.viewCount = s.viewCount + :delta WHERE s.id = :id")
    void incrementViewCount(Long id, int delta);
}

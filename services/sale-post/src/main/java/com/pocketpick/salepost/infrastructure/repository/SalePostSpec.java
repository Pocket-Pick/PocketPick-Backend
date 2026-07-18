package com.pocketpick.salepost.infrastructure.repository;

import com.pocketpick.salepost.domain.domain.SalePost;
import com.pocketpick.salepost.domain.domain.SaleStatus;
import org.springframework.data.jpa.domain.Specification;

public class SalePostSpec {

    public static Specification<SalePost> filter(Long cardId, SaleStatus status) {
        return Specification
                .where(hasCardId(cardId))
                .and(hasStatus(status));
    }

    private static Specification<SalePost> hasCardId(Long cardId) {
        return (root, query, cb) ->
                cardId == null ? null : cb.equal(root.get("cardId"), cardId);
    }

    private static Specification<SalePost> hasStatus(SaleStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }
}

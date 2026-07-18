package com.pocketpick.chat.domain.room;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    Optional<ChatRoom> findByBuyerIdAndSellerIdAndSalePostId(Long buyerId, Long sellerId, Long salePostId);

    List<ChatRoom> findByBuyerIdOrSellerIdOrderByUpdatedAtDesc(Long buyerId, Long sellerId);
}

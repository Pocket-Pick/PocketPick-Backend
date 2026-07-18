package com.pocketpick.chat.domain.room;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    Optional<ChatRoom> findByBuyerIdAndSellerIdAndSalePostId(Long buyerId, Long sellerId, Long salePostId);

    List<ChatRoom> findByBuyerIdOrSellerIdOrderByUpdatedAtDesc(Long buyerId, Long sellerId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'lastMessage': ?1 } }")
    void updateLastMessage(String roomId, String content);
}

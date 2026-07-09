package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.room.ChatRoom;
import com.pocketpick.chat.domain.room.ChatRoomRepository;
import com.pocketpick.chat.domain.room.ChatRoomUseCase;
import com.pocketpick.chat.domain.room.dto.ChatRoomResponse;
import com.pocketpick.chat.domain.room.dto.CreateChatRoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService implements ChatRoomUseCase {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    @Override
    public ChatRoomResponse createOrGet(CreateChatRoomRequest request) {
        return chatRoomRepository
                .findByBuyerIdAndSellerIdAndSalePostId(
                        request.buyerId(), request.sellerId(), request.salePostId())
                .map(ChatRoomResponse::from)
                .orElseGet(() -> {
                    ChatRoom room = ChatRoom.builder()
                            .buyerId(request.buyerId())
                            .sellerId(request.sellerId())
                            .salePostId(request.salePostId())
                            .build();
                    return ChatRoomResponse.from(chatRoomRepository.save(room));
                });
    }

    @Override
    public List<ChatRoomResponse> listRooms(Long userId) {
        return chatRoomRepository
                .findByBuyerIdOrSellerIdOrderByUpdatedAtDesc(userId, userId)
                .stream()
                .map(ChatRoomResponse::from)
                .toList();
    }
}

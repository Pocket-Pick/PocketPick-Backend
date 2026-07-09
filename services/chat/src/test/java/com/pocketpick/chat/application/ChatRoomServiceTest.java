package com.pocketpick.chat.application;

import com.pocketpick.chat.domain.room.ChatRoom;
import com.pocketpick.chat.domain.room.ChatRoomRepository;
import com.pocketpick.chat.domain.room.dto.ChatRoomResponse;
import com.pocketpick.chat.domain.room.dto.CreateChatRoomRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@DisplayName("ChatRoomService")
@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Nested
    @DisplayName("채팅방 생성 또는 조회")
    class CreateOrGet {

        @Test
        @DisplayName("중복 채팅방이 없으면 새로 생성한다")
        void createOrGet_noDuplicate_createsNewRoom() {
            // given
            CreateChatRoomRequest request = new CreateChatRoomRequest(1L, 2L, 10L);
            ChatRoom saved = ChatRoom.builder().buyerId(1L).sellerId(2L).salePostId(10L).build();

            given(chatRoomRepository.findByBuyerIdAndSellerIdAndSalePostId(1L, 2L, 10L))
                    .willReturn(Optional.empty());
            given(chatRoomRepository.save(any())).willReturn(saved);

            // when
            ChatRoomResponse response = chatRoomService.createOrGet(request);

            // then
            verify(chatRoomRepository).save(any());
            assertThat(response.buyerId()).isEqualTo(1L);
            assertThat(response.sellerId()).isEqualTo(2L);
            assertThat(response.salePostId()).isEqualTo(10L);
        }

        @Test
        @DisplayName("중복 채팅방이 있으면 기존 채팅방을 반환한다")
        void createOrGet_duplicateExists_returnsExistingRoom() {
            // given
            CreateChatRoomRequest request = new CreateChatRoomRequest(1L, 2L, 10L);
            ChatRoom existing = ChatRoom.builder().buyerId(1L).sellerId(2L).salePostId(10L).build();

            given(chatRoomRepository.findByBuyerIdAndSellerIdAndSalePostId(1L, 2L, 10L))
                    .willReturn(Optional.of(existing));

            // when
            ChatRoomResponse response = chatRoomService.createOrGet(request);

            // then
            verify(chatRoomRepository, never()).save(any());
            assertThat(response.buyerId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("채팅방 목록 조회")
    class ListRooms {

        @Test
        @DisplayName("userId로 참여한 모든 채팅방을 최신순으로 반환한다")
        void listRooms_returnsRoomsOrderedByUpdatedAt() {
            // given
            Long userId = 1L;
            ChatRoom room1 = ChatRoom.builder().buyerId(1L).sellerId(2L).salePostId(10L).build();
            ChatRoom room2 = ChatRoom.builder().buyerId(3L).sellerId(1L).salePostId(20L).build();

            given(chatRoomRepository.findByBuyerIdOrSellerIdOrderByUpdatedAtDesc(userId, userId))
                    .willReturn(List.of(room1, room2));

            // when
            List<ChatRoomResponse> responses = chatRoomService.listRooms(userId);

            // then
            assertThat(responses).hasSize(2);
        }

        @Test
        @DisplayName("참여한 채팅방이 없으면 빈 목록을 반환한다")
        void listRooms_noRooms_returnsEmptyList() {
            // given
            given(chatRoomRepository.findByBuyerIdOrSellerIdOrderByUpdatedAtDesc(99L, 99L))
                    .willReturn(List.of());

            // when
            List<ChatRoomResponse> responses = chatRoomService.listRooms(99L);

            // then
            assertThat(responses).isEmpty();
        }
    }
}

package com.pocketpick.chat.domain.room;

import com.pocketpick.chat.domain.room.dto.ChatRoomResponse;
import com.pocketpick.chat.domain.room.dto.CreateChatRoomRequest;

import java.util.List;

public interface ChatRoomUseCase {
    ChatRoomResponse createOrGet(CreateChatRoomRequest request);
    List<ChatRoomResponse> listRooms(Long userId);
}

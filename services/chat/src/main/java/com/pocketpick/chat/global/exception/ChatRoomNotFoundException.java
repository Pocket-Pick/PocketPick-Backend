package com.pocketpick.chat.global.exception;

import com.pocketpick.chat.domain.exception.ChatErrorCode;

public class ChatRoomNotFoundException extends BusinessException {

    public ChatRoomNotFoundException() {
        super(ChatErrorCode.CHAT_ROOM_NOT_FOUND);
    }
}

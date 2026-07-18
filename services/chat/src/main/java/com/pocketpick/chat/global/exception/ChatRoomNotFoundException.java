package com.pocketpick.chat.global.exception;

import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends BusinessException {

    public ChatRoomNotFoundException() {
        super("CHAT_ROOM_NOT_FOUND", "채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}

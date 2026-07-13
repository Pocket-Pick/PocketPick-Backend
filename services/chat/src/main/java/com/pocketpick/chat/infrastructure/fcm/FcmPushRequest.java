package com.pocketpick.chat.infrastructure.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FcmPushRequest {

    private Long userId;
    private String roomId;
    private Long senderId;
    private String content;
}

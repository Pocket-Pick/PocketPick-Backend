package com.pocketpick.chat.infrastructure.fcm;

public interface FcmPushUseCase {

    void sendPush(FcmPushRequest request);
}

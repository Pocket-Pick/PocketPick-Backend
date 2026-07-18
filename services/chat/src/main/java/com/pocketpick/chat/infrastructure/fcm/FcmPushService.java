package com.pocketpick.chat.infrastructure.fcm;

public interface FcmPushService {

    void sendPush(FcmPushRequest request);
}

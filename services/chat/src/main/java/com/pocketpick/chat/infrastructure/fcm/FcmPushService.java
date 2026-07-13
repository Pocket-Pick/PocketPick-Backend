package com.pocketpick.chat.infrastructure.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmPushService implements FcmPushUseCase {

    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void sendPush(FcmPushRequest request) {
        fcmTokenRepository.find(request.getUserId()).ifPresentOrElse(
                fcmToken -> send(request, fcmToken),
                () -> log.info("FCM token not found: userId={}", request.getUserId())
        );
    }

    private void send(FcmPushRequest request, String fcmToken) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle("새 메시지가 도착했습니다")
                        .setBody(request.getContent())
                        .build())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM push failed: userId={}", request.getUserId(), e);
        }
    }
}

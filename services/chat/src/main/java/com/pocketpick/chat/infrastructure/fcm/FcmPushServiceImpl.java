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
public class FcmPushServiceImpl implements FcmPushUseCase {

    private final FcmTokenRepository fcmTokenRepository;

    @Override
    public void sendPush(Long userId, String content) {
        fcmTokenRepository.find(userId).ifPresentOrElse(
                fcmToken -> send(userId, fcmToken, content),
                () -> log.info("FCM token not found: userId={}", userId)
        );
    }

    private void send(Long userId, String fcmToken, String content) {
        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle("새 메시지가 도착했습니다")
                        .setBody(content)
                        .build())
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            log.error("FCM push failed: userId={}", userId, e);
        }
    }
}

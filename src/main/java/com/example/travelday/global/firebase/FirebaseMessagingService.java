package com.example.travelday.global.firebase;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FirebaseMessagingService {

    /**
     * Firebase를 통해 푸시 알림을 전송합니다.
     */
    public String sendNotification(String token, String content) {
        Notification notification = Notification.builder()
                .setTitle("당신을 기다리고 있어요!")
                .setBody(content)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            log.info("Sending FCM message to token: {}", token);
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent FCM message. Message ID: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode().equals(MessagingErrorCode.INVALID_ARGUMENT)) {
                // 토큰이 유효하지 않은 경우, 오류 코드를 반환
                return e.getMessagingErrorCode().toString();
            } else if (e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                // 재발급된 이전 토큰인 경우, 오류 코드를 반환
                return e.getMessagingErrorCode().toString();
            }
            else { // 그 외, 오류는 런타임 예외로 처리
                throw new RuntimeException(e);
            }
        }
    }
}

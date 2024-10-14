package com.example.travelday.global.firebase;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DependsOn("firebaseInitializer")
public class FirebaseMessagingService {

    /**
     * Firebase를 통해 푸시 알림을 전송합니다.
     */
    public String sendNotification(String token, String content) {
        String INVITATION_WORD = "당신을 기다리고 있어요!";

        Notification notification = Notification.builder()
                .setTitle(INVITATION_WORD)
                .setBody(content)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            // 토큰이 유효하지 않거나 재발급된 토큰인 경우 오류 코드를 반환
            if (e.getMessagingErrorCode().equals(MessagingErrorCode.INVALID_ARGUMENT) || e.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                return e.getMessagingErrorCode().toString();
            } else { // 그 외, 오류는 런타임 예외로 처리
                throw new RuntimeException(e);
            }
        }
    }
}

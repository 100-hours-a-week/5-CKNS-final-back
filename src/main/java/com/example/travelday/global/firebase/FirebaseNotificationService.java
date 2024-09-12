package com.example.travelday.global.firebase;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.fcm.entity.FcmToken;
import com.example.travelday.domain.fcm.repository.FcmTokenRepository;
import com.example.travelday.domain.fcm.service.FcmService;
import com.example.travelday.domain.invitation.entity.Invitation;
import com.example.travelday.domain.notification.dto.request.NotificationReqDto;
import com.example.travelday.domain.notification.service.NotificationService;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseNotificationService {

    private final FcmTokenRepository fcmTokenRepository;

    private final NotificationService notificationService;

    private final FcmService fcmService;

    private final FirebaseMessagingService firebaseMessagingService;

    @Transactional
    public void notifyNewInvitation(Member member, Invitation invitation) {
        String notificationContent = invitation.getInviter().getNickname() + "님이 "
                                    + invitation.getTravelRoom().getName() + "에 초대했습니다.";

        // 알림 생성 및 전송
        NotificationReqDto notificationReqDto = NotificationReqDto.builder()
                .userId(member.getUserId())
                .notificationContent(notificationContent)
                .travelRoomId(invitation.getTravelRoom().getId())
                .build();
        notificationService.createNotification(notificationReqDto);

        // 모든 fcm 토큰 가져오기
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMember(member);

        for (FcmToken fcmToken : fcmTokens) {
            fcmService.updateLastUsedTime(fcmToken);
            sendNotificationToUser(fcmToken, notificationContent);
        }
    }

    private void sendNotificationToUser(FcmToken fcmToken, String content) {
        String response = firebaseMessagingService.sendNotification(fcmToken.getToken(), content);

        if (MessagingErrorCode.INVALID_ARGUMENT.name().equals(response) || MessagingErrorCode.UNREGISTERED.name().equals(response)) {
            // 토큰이 유효하지 않은 경우, 삭제
            fcmTokenRepository.delete(fcmToken);
        } else {
            log.info(fcmToken.getToken());
        }
    }
}

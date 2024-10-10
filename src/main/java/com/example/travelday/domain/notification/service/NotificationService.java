package com.example.travelday.domain.notification.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.invitation.entity.Invitation;
import com.example.travelday.domain.invitation.repository.InvitationRepository;
import com.example.travelday.domain.notification.dto.request.NotificationReqDto;
import com.example.travelday.domain.notification.dto.response.NotificationResDto;
import com.example.travelday.domain.notification.entity.Notification;
import com.example.travelday.domain.notification.repository.NotificationRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;

    private final NotificationRepository notificationRepository;

    private final InvitationRepository invitationRepository;

    @Transactional
    public void createNotification(NotificationReqDto notificationReqDto) {
        Member member = memberRepository.findByUserId(notificationReqDto.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Notification notification = Notification.builder()
                .member(member)
                .content(notificationReqDto.notificationContent())
                .travelRoomId(notificationReqDto.travelRoomId())
                .build();

        notificationRepository.save(notification);
    }

    // 특정 멤버의 모든 알람 조회
    @Transactional
    public List<NotificationResDto> getNotificationsListByMember(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Notification> notificationList = notificationRepository.findAllByMemberAndIsCheckedFalseOrderByCreatedTimeDesc(member);
        List<NotificationResDto> notificationResDtoList = new ArrayList<>();

        for (Notification notification : notificationList) {
            Invitation invitation = invitationRepository.findByInviteeAndTravelRoomId(notification.getMember(), notification.getTravelRoomId());
            notificationResDtoList.add(NotificationResDto.of(notification, invitation.getId()));
        }

        // notificationList의 Notification 객체들을 NotificationResDto로 변환하여 리스트로 저장
        return notificationResDtoList;
    }
}

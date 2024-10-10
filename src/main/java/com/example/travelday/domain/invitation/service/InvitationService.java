package com.example.travelday.domain.invitation.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.invitation.dto.request.InvitationReqDto;
import com.example.travelday.domain.invitation.entity.Invitation;
import com.example.travelday.domain.invitation.enums.InvitationResponseStatus;
import com.example.travelday.domain.invitation.enums.InvitationStatus;
import com.example.travelday.domain.invitation.repository.InvitationRepository;
import com.example.travelday.domain.notification.entity.Notification;
import com.example.travelday.domain.notification.repository.NotificationRepository;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.entity.UserTravelRoom;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.example.travelday.global.firebase.FirebaseNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final FirebaseNotificationService firebaseNotificationService;

    private final TravelRoomRepository travelRoomRepository;

    private final MemberRepository memberRepository;

    private final InvitationRepository invitationRepository;

    private final UserTravelRoomRepository userTravelRoomRepository;

    private final NotificationRepository notificationRepository;

    @Transactional
    public void createInvitation(Long travelRoomId, InvitationReqDto invitationReqDto, String userId) {
        // TravelRoom 있는 지 검증
        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                        .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        // 여행방이 종료된 경우 예외 처리 (예시로 endDate와 비교)
//        LocalDate endDate = LocalDate.parse(travelRoom.getEndDate());
//        if (endDate.isBefore(LocalDate.now())) {
//            throw new CustomException(ErrorCode.TRAVEL_ROOM_CLOSED);
//        }
        // 보내는 이 Member에 있는 지 검증
        Member sender = memberRepository.findByUserId(userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        // 받는 이 Member 에 있는지 검증
        Member receiver = memberRepository.findByUserId(invitationReqDto.invitee())
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 자기 자신에게 초대 보낼 경우 예외 처리
        if (sender.equals(receiver)) {
            throw new CustomException(ErrorCode.CANNOT_INVITE_SELF);
        }

        // 받는 이가 이미 TravelRoom에 있는지 검증
        if (userTravelRoomRepository.existsByTravelRoomIdAndMember(travelRoomId, receiver)) {
            throw new CustomException(ErrorCode.ALREADY_IN_TRAVELROOM);
        }

        Invitation invitation = invitationRepository.findByInviteeAndTravelRoomId(receiver, travelRoomId);

        if (invitation == null) {
            createNewInvitation(travelRoom, sender, receiver);
        } else if (invitation.getStatus().equals(InvitationStatus.REJECTED)) {
            resendInvitation(invitation, receiver);
        } else if (invitation.getStatus().equals(InvitationStatus.PENDING)) {
            throw new CustomException(ErrorCode.ALREADY_SEND_INVITATION);
        } else if (invitation.getStatus().equals(InvitationStatus.ACCEPTED)) {
            throw new CustomException(ErrorCode.ALREADY_IN_TRAVELROOM);
        } else {
            throw new CustomException(ErrorCode.INVALID_INVITATION_STATUS);
        }
    }

    @Transactional
    public void responseInvitation(String userId, Long travelRoomId, Long invitationId, String status) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVITATION_NOT_FOUND));

        if (status.equals(InvitationResponseStatus.Y.toString())) {
            invitation.accept();
        } else if (status.equals(InvitationResponseStatus.N.toString())) {
            invitation.reject();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST_FLAG);
        }

        invitationRepository.save(invitation);

        UserTravelRoom userTravelRoom = UserTravelRoom
                .create(travelRoom, member);
        userTravelRoomRepository.save(userTravelRoom);

        Notification notification = notificationRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.check();

        notificationRepository.save(notification);
    }

    private void createNewInvitation(TravelRoom travelRoom, Member sender, Member receiver) {
        Invitation newInvitation = Invitation.builder()
                .travelRoom(travelRoom)
                .inviter(sender)
                .invitee(receiver)
                .build();

        invitationRepository.save(newInvitation);
        firebaseNotificationService.notifyNewInvitation(receiver, newInvitation); // 파이어베이스 메세지 송신
    }

    private void resendInvitation(Invitation invitation, Member receiver) {
        invitation.resendInvitation();
        invitationRepository.save(invitation);
        firebaseNotificationService.notifyNewInvitation(receiver, invitation);
    }
}

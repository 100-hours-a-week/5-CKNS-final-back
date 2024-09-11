package com.example.travelday.domain.invitation.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.invitation.dto.request.InvitationReqDto;
import com.example.travelday.domain.invitation.entity.Invitation;
import com.example.travelday.domain.invitation.repository.InvitationRepository;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.example.travelday.global.firebase.FirebaseNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final FirebaseNotificationService firebaseNotificationService;

    private final TravelRoomRepository travelRoomRepository;

    private final MemberRepository memberRepository;

    private final InvitationRepository invitationRepository;

    @Transactional
    public void createInvitation(Long travelRoomId, InvitationReqDto invitationReqDto, String userId) {
        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                        .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        Member sender = memberRepository.findByUserId(userId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member receiver = memberRepository.findByUserId(invitationReqDto.invitee())
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Invitation invitation = Invitation.builder()
                .travelRoom(travelRoom)
                .inviter(sender)
                .invitee(receiver)
                .build();
        invitationRepository.save(invitation);

        firebaseNotificationService.notifyNewInvitation(receiver, invitation); // 파이어베이스 메세지 송신
    }
}

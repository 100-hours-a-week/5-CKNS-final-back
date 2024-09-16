package com.example.travelday.domain.settlement.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.settlement.dto.response.SettlementResDto;
import com.example.travelday.domain.settlement.repository.SettlementRepository;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final MemberRepository memberRepository;

    private final SettlementRepository settlementRepository;

    private final UserTravelRoomRepository userTravelRoomRepository;

    @Transactional(readOnly = true)
    public SettlementResDto getAllSettlement(Long travelRoomId, String userId) {

        // TODO: 이 부분 리팩토링 -> 전역 메서드로 관리
        Member member = memberRepository.findByUserId(userId)
                                            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isMemberInTravelRoom = userTravelRoomRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                                            .isPresent();

        if (!isMemberInTravelRoom) {
            throw new CustomException(ErrorCode.USER_DOES_NOT_JOIN_TRAVEL_ROOM);
        }

        return SettlementResDto.fromEntity(settlementRepository.findByTravelRoomId(travelRoomId)
                                            .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND)));
    }

}

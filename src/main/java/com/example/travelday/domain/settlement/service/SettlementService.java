package com.example.travelday.domain.settlement.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.settlement.dto.request.SettlementDetailReqDto;
import com.example.travelday.domain.settlement.dto.response.SettlementDetailResDto;
import com.example.travelday.domain.settlement.dto.response.SettlementResDto;
import com.example.travelday.domain.settlement.entity.Settlement;
import com.example.travelday.domain.settlement.entity.SettlementDetail;
import com.example.travelday.domain.settlement.repository.SettlementDetailRepository;
import com.example.travelday.domain.settlement.repository.SettlementRepository;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final MemberRepository memberRepository;

    private final SettlementRepository settlementRepository;

    private final SettlementDetailRepository settlementDetailRepository;

    private final UserTravelRoomRepository userTravelRoomRepository;

    @Transactional(readOnly = true)
    public SettlementResDto getAllSettlement(Long travelRoomId, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        return SettlementResDto.fromEntity(settlementRepository.findByTravelRoomId(travelRoomId)
                                            .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    public List<SettlementDetailResDto> getSettlementDetailList(Long travelRoomId, Long settlementId, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        validateSettlementInTraveRoom(settlementId, travelRoomId);

        List<SettlementDetail> settlementDetails = settlementDetailRepository.findBySettlementId(settlementId);

        return settlementDetails.stream()
                    .map(detail -> SettlementDetailResDto.fromEntity(detail))
                    .collect(Collectors.toList());
    }

    @Transactional
    public void addSettlement(Long travelRoomId, Long settlementId, SettlementDetailReqDto settlementDetailReqDto, String username) {

        validateMemberInTravelRoom(username, travelRoomId);

        validateSettlementInTraveRoom(settlementId, travelRoomId);

        Settlement settlement = settlementRepository.findById(settlementId)
                                .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND));

        SettlementDetail settlementDetail = SettlementDetail.addOf(settlement, settlementDetailReqDto);

        settlementDetailRepository.save(settlementDetail);
    }

    // 여행방에 멤버가 있는지 확인
    private void validateMemberInTravelRoom(String userId, Long travelRoomId) {
        Member member = memberRepository.findByUserId(userId)
                            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean exists = userTravelRoomRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                            .isPresent();

        if (!exists) {
            throw new CustomException(ErrorCode.USER_NOT_IN_TRAVEL_ROOM);
        }
    }

    // 여행방에 정산이 있는지 확인
    private void validateSettlementInTraveRoom(Long settlementId, Long travelRoomId) {
        boolean exists = settlementRepository.findByIdAndTravelRoomId(settlementId, travelRoomId)
                            .isPresent();

        if (!exists) {
            throw new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND);
        }
    }


}

package com.example.travelday.domain.settlement.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.settlement.dto.request.SettlementDetailReqDto;
import com.example.travelday.domain.settlement.dto.request.SettlementNotificationReqDto;
import com.example.travelday.domain.settlement.dto.response.SettlementDetailResDto;
import com.example.travelday.domain.settlement.dto.response.SettlementResDto;
import com.example.travelday.domain.settlement.entity.Settlement;
import com.example.travelday.domain.settlement.entity.SettlementDetail;
import com.example.travelday.domain.settlement.repository.SettlementDetailRepository;
import com.example.travelday.domain.settlement.repository.SettlementRepository;
import com.example.travelday.domain.settlement.utils.SettlementDetailChangedEvent;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.entity.UserTravelRoom;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.example.travelday.global.firebase.FirebaseNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final MemberRepository memberRepository;

    private final SettlementRepository settlementRepository;

    private final SettlementDetailRepository settlementDetailRepository;

    private final UserTravelRoomRepository userTravelRoomRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final FirebaseNotificationService firebaseNotificationService;

    private final TravelRoomRepository travelRoomRepository;

    @Transactional
    public void createSettlement(Long travelRoomId, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                                    .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        Settlement settlement = Settlement.builder()
                                    .travelRoom(travelRoom)
                                    .totalAmount(BigDecimal.ZERO)
                                    .build();

        settlementRepository.save(settlement);
    }

    @Transactional(readOnly = true)
    public List<SettlementResDto> getAllSettlement(Long travelRoomId, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        List<Settlement> settlements = settlementRepository.findByTravelRoomId(travelRoomId)
                                            .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND));

        return settlements.stream()
                    .map(settlement -> SettlementResDto.fromEntity(settlement))
                    .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<SettlementResDto> getAllSettlements(Long travelRoomId, String userId) {

        validateMemberInTravelRoom(userId,travelRoomId);

        List<Settlement> settlements = settlementRepository.findAllByTravelRoomId(travelRoomId);

        return settlements.stream()
                .map(SettlementResDto::fromEntity)
                .collect(Collectors.toList());
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
    public void addSettlement(Long travelRoomId, Long settlementId, SettlementDetailReqDto settlementDetailReqDto, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        validateSettlementInTraveRoom(settlementId, travelRoomId);

        Settlement settlement = settlementRepository.findById(settlementId)
                                .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND));

        validateAmount(BigDecimal.valueOf(settlementDetailReqDto.amount()));

        SettlementDetail settlementDetail = SettlementDetail.addOf(settlement, settlementDetailReqDto);

        settlementDetailRepository.save(settlementDetail);

        eventPublisher.publishEvent(new SettlementDetailChangedEvent(this, settlementId));
    }

    @Transactional
    public void updateSettlementDetail(Long travelRoomId, Long settlementId, Long settlementDetailId, SettlementDetailReqDto settlementDetailReqDto, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        validateSettlementInTraveRoom(settlementId, travelRoomId);

        SettlementDetail settlementDetail = settlementDetailRepository.findById(settlementDetailId)
                                            .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_DETAIL_NOT_FOUND));

        validateAmount(BigDecimal.valueOf(settlementDetailReqDto.amount()));

        settlementDetail.updateDetails(settlementDetailReqDto);

        settlementDetailRepository.save(settlementDetail);

        eventPublisher.publishEvent(new SettlementDetailChangedEvent(this, settlementId));
    }

    @Transactional
    public void deleteSettlementDetail(Long travelRoomId, Long settlementId, Long settlementDetailId, String username) {

        validateMemberInTravelRoom(username, travelRoomId);

        validateSettlementInTraveRoom(settlementId, travelRoomId);

        SettlementDetail settlementDetail = settlementDetailRepository.findById(settlementDetailId)
                                            .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_DETAIL_NOT_FOUND));

        settlementDetailRepository.delete(settlementDetail);

        eventPublisher.publishEvent(new SettlementDetailChangedEvent(this, settlementId));
    }

    @Transactional
    public void deleteSettlement(Long travelRoomId, Long settlementId, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        validateSettlementInTraveRoom(settlementId, travelRoomId);

        Settlement settlement = settlementRepository.findById(settlementId)
                                .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND));

        settlementRepository.delete(settlement);
    }

    public void notifySettlement(Long travelRoomId, SettlementNotificationReqDto settlementNotificationReqDto, String userId) {
        validateMemberInTravelRoom(userId, travelRoomId);

        // 여행방의 모든 멤버를 가져옴
        List<UserTravelRoom> userTravelRooms = userTravelRoomRepository.findByTravelRoomId(travelRoomId)
                                                        .orElseThrow(() -> new CustomException(ErrorCode.USER_DOES_NOT_JOIN_TRAVEL_ROOM));

        // 여행방의 모든 멤버들의 닉네임 목록을 가져옴
        List<String> memberNicknames = userTravelRooms.stream()
                                            .map(userTravelRoom -> userTravelRoom.getMember().getNickname())
                                            .collect(Collectors.toList());

        settlementNotificationReqDto.nicknameToPrice().keySet().stream()
                                .filter(nickname -> !memberNicknames.contains(nickname))
                                .findFirst()  // 필터링된 첫 번째 닉네임을 찾음
                                .ifPresent(nickname -> { // 존재할 경우 예외를 던짐
                throw new CustomException(ErrorCode.USER_NOT_IN_TRAVEL_ROOM);
            });


        firebaseNotificationService.notifySettlement(userId, settlementNotificationReqDto, travelRoomId); // 파이어베이스 메세지 송신
    }

    // 여행방에 멤버가 있는지 확인
    private void validateMemberInTravelRoom(String userId, Long travelRoomId) {
        Member member = memberRepository.findByUserId(userId)
                            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        UserTravelRoom userTravelRoom = userTravelRoomRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                                                .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));
    }

    // 여행방에 정산이 있는지 확인
    private void validateSettlementInTraveRoom(Long settlementId, Long travelRoomId) {
        Settlement settlement = settlementRepository.findByIdAndTravelRoomId(settlementId, travelRoomId)
                                .orElseThrow(() -> new CustomException(ErrorCode.SETTLEMENT_NOT_FOUND));
    }

    // 정산 금액 범위 확인
    public void validateAmount(BigDecimal amount) {
        if (amount.precision() > 10 || amount.scale() > 2) {
            throw new CustomException(ErrorCode.INVALID_AMOUNT_RANGE);
        }
    }
}

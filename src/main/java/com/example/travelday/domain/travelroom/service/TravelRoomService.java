package com.example.travelday.domain.travelroom.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.travelroom.dto.request.TravelRoomReqDto;
import com.example.travelday.domain.travelroom.dto.response.TravelRoomResDto;
import com.example.travelday.domain.travelroom.dto.request.TravelRoomCreateReqDto;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.entity.UserTravelRoom;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelRoomService {

    private final TravelRoomRepository travelRoomRepository;

    private final UserTravelRoomRepository userTravelRoomRepository;

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<TravelRoomResDto> getAllTravelRoom() {
        return travelRoomRepository.findAll()
                    .stream()
                    .map(TravelRoomResDto::fromEntity)
                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TravelRoomResDto getTravelRoomById(Long travelRoomId) {
        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                                    .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));
        return TravelRoomResDto.fromEntity(travelRoom);
    }

    @Transactional
    public void createTravelRoom(TravelRoomCreateReqDto requestDto) {
        // TODO: @AuthenticationPrincipal 사용해서 현재 로그인한 사용자 정보 가져오기, 현재는 임시로 userId로 사용
        TravelRoom travelRoom = TravelRoom.addOf(requestDto);
        TravelRoom savedTravelRoom = travelRoomRepository.save(travelRoom);
        log.info("TravelRoom created: {}", savedTravelRoom);

        Member member = memberRepository.findByUserId(requestDto.userId());

        UserTravelRoom userTravelRoom = UserTravelRoom.create(savedTravelRoom, member);
        userTravelRoomRepository.save(userTravelRoom);
    }

    @Transactional
    public void updateTravelRoom (Long travelRoomId, TravelRoomReqDto requestDto) {
        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                                    .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        travelRoom.update(requestDto);
        travelRoomRepository.save(travelRoom);
    }

    @Transactional
    public void deleteTravelRoom(Long travelRoomId) {
        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                                    .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));
        travelRoomRepository.delete(travelRoom);
    }
}
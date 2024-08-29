package com.example.travelday.domain.travelroom.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.travelroom.dto.request.UpdateTravelPlanListDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanListReqDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanOverwriteDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanReqDto;
import com.example.travelday.domain.travelroom.dto.response.TravelPlanResDto;
import com.example.travelday.domain.travelroom.entity.TravelPlan;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.repository.TravelPlanRepository;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;

    private final TravelRoomRepository travelRoomRepository;

    private final MemberRepository memberRepository;

    private final UserTravelRoomRepository userTravelRoomRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public List<TravelPlanResDto> getAllTravelPlan(Long travelRoomId, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        List<TravelPlan> travelPlans = travelPlanRepository.findAllByTravelRoomId(travelRoomId);
        List<TravelPlanResDto> travelPlanResDtos = new ArrayList<>();

        for (TravelPlan travelPlan : travelPlans) {
            travelPlanResDtos.add(TravelPlanResDto.of(travelPlan));
        }
        return travelPlanResDtos;
    }

    @Transactional
    public void addTravelPlanList(Long travelRoomId, TravelPlanListReqDto travelPlanListReqDto, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                                    .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        Integer maxPosition = travelPlanRepository.findMaxOrderByTravelRoomIdAndScheduledDay(travelRoomId, 0)
                                    .orElse(0);

        int position = maxPosition + 1;

        for (TravelPlanReqDto planReqDto : travelPlanListReqDto.body()) {
            TravelPlan travelPlan = TravelPlan.addOf(planReqDto, travelRoom, position);
            travelPlanRepository.save(travelPlan);
            position++;
        }
    }

    @Transactional
    public void addTravelPlanDirect(Long travelRoomId, TravelPlanReqDto travelPlanReqDto, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                                    .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        Integer maxPosition = travelPlanRepository.findMaxOrderByTravelRoomIdAndScheduledDay(travelRoomId, travelPlanReqDto.scheduledDay())
                                .orElse(0);

        int position = maxPosition + 1;

        TravelPlan travelPlan = TravelPlan.addOf(travelPlanReqDto, travelRoom, position);
        travelPlanRepository.save(travelPlan);
    }

    @Transactional
    public boolean checkUsing(Long travelRoomId, String userId) {
        validateMemberInTravelRoom(userId, travelRoomId);

        String redisKey = "Using Updating:" + travelRoomId;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String cachedData = (String) valueOperations.get(redisKey);

        log.info(cachedData);
        log.info(userId);

        if (cachedData != null && !cachedData.equals(userId)) {
            log.info("======== 캐시가 있고 데이터는 그 데이터는 userId가 아님");
            return false;
        } else if (cachedData != null && cachedData.equals(userId)) {
            log.info("======== 캐시가 있고 데이터는 그 데이터는 userId가 맞음");
            return true;
        }

        valueOperations.set(redisKey, userId, Duration.ofSeconds(1200000));

        return true;
    }

    @Transactional
    public void updateTravelPlan(Long travelRoomId, UpdateTravelPlanListDto updateTravelPlanListDto, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        List<TravelPlanOverwriteDto> overwritePlans = updateTravelPlanListDto.body();

        for (TravelPlanOverwriteDto dto : overwritePlans) {
            travelPlanRepository.updateTravelPlan(dto.id(), dto.scheduledDay(), dto.position());

            log.info("Updated TravelPlan: id={}, scheduledDay={}, order={}",
                dto.id(), dto.scheduledDay(), dto.position());
        }
    }


    @Transactional
    public void deleteTravelPlan(Long travelRoomId, Long travelPlanId, String userId) {

        validateMemberInTravelRoom(userId, travelRoomId);

        TravelPlan travelPlan = travelPlanRepository.findById(travelPlanId)
                                .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_PLAN_NOT_FOUND));

        travelPlanRepository.delete(travelPlan);
    }

    // TODO: Move this method to a separate class
    private void validateMemberInTravelRoom(String userId, Long travelRoomId) {
        Member member = memberRepository.findByUserId(userId)
                            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean exists = userTravelRoomRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                                .isPresent();

        if (!exists) {
            throw new CustomException(ErrorCode.USER_NOT_IN_TRAVEL_ROOM);
        }
    }

}
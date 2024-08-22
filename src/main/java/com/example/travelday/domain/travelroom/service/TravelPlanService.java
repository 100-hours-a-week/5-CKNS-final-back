package com.example.travelday.domain.travelroom.service;

import com.example.travelday.domain.travelroom.dto.request.TravelPlanListOverwriteDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanListReqDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanOverwriteDto;
import com.example.travelday.domain.travelroom.dto.request.TravelPlanReqDto;
import com.example.travelday.domain.travelroom.dto.response.TravelPlanResDto;
import com.example.travelday.domain.travelroom.entity.TravelPlan;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.repository.TravelPlanRepository;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.WishRepository;
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
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;

    private final TravelRoomRepository travelRoomRepository;

    private final WishRepository wishRepository;

    @Transactional
    public List<TravelPlanResDto> getAllTravelPlan(Long travelRoomId) {

        List<TravelPlan> travelPlans = travelPlanRepository.findAllByTravelRoomId(travelRoomId);
        List<TravelPlanResDto> travelPlanResDtos = new ArrayList<>();

        for (TravelPlan travelPlan : travelPlans) {
            travelPlanResDtos.add(TravelPlanResDto.of(travelPlan));
        }
        return travelPlanResDtos;
    }

    @Transactional
    public void addTravelPlanList(Long travelRoomId, TravelPlanListReqDto travelPlanListReqDto) {

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
    public void addTravelPlanDirect(Long travelRoomId, TravelPlanReqDto travelPlanReqDto) {

        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                                    .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        Integer maxPosition = travelPlanRepository.findMaxOrderByTravelRoomIdAndScheduledDay(travelRoomId, travelPlanReqDto.scheduledDay())
                                .orElse(0);

        int position = maxPosition + 1;

        TravelPlan travelPlan = TravelPlan.addOf(travelPlanReqDto, travelRoom, position);
        travelPlanRepository.save(travelPlan);
    }

    @Transactional
    public void updateTravelPlan(Long travelRoomId, TravelPlanListOverwriteDto travelPlanListOverwriteDto) {

        List<TravelPlanOverwriteDto> overwritePlans = travelPlanListOverwriteDto.body();

        for (TravelPlanOverwriteDto dto : overwritePlans) {
            travelPlanRepository.updateTravelPlan(dto.id(), dto.scheduledDay(), dto.position());

            log.info("Updated TravelPlan: id={}, scheduledDay={}, order={}",
                dto.id(), dto.scheduledDay(), dto.position());
        }
    }


    @Transactional
    public void deleteTravelPlan(Long travelRoomId, Long travelPlanId) {

        TravelPlan travelPlan = travelPlanRepository.findById(travelPlanId)
                                .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_PLAN_NOT_FOUND));

        travelPlanRepository.delete(travelPlan);
    }
}
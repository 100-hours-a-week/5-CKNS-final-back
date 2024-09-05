package com.example.travelday.domain.travelplan.dto.response;

import com.example.travelday.domain.travelplan.entity.TravelPlan;
import lombok.Builder;

@Builder
public record TravelPlanResDto (
        Long id,
        String name,
        int scheduledDay,
        int position,
        double latitude,
        double longitude
) {
    public static TravelPlanResDto of(TravelPlan travelPlan) {
        return TravelPlanResDto.builder()
                .id(travelPlan.getId())
                .name(travelPlan.getName())
                .scheduledDay(travelPlan.getScheduledDay())
                .position(travelPlan.getPosition())
                .latitude(travelPlan.getLatitude())
                .longitude(travelPlan.getLongitude())
                .build();
    }
}

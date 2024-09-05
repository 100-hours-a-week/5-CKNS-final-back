package com.example.travelday.domain.travelplan.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TravelPlanReqDto (

    @NotBlank
    String name,

    @NotNull
    int scheduledDay,

    @NotNull
    double latitude,

    @NotNull
    double longitude
) {
}

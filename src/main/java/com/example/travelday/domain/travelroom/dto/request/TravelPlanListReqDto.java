package com.example.travelday.domain.travelroom.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelPlanListReqDto (

    @NotEmpty
    List<TravelPlanReqDto> body

) {
}
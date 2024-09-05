package com.example.travelday.domain.travelplan.dto.request.group;

import com.example.travelday.domain.travelplan.dto.request.TravelPlanReqDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record TravelPlansReqDto(

    @NotEmpty
    List<TravelPlanReqDto> body

) {
}
package com.example.travelday.domain.travelplan.dto.request.batch;

import com.example.travelday.domain.travelplan.dto.request.TravelPlanOverwriteReqDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateTravelPlanListReqDto(

    @NotEmpty
    List<TravelPlanOverwriteReqDto> body

) {
}

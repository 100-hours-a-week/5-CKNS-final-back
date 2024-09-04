package com.example.travelday.domain.travelplan.dto.request.batch;

import com.example.travelday.domain.travelplan.dto.request.TravelPlanOverwriteDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateTravelPlanListDto(

    @NotEmpty
    List<TravelPlanOverwriteDto> body

) {
}

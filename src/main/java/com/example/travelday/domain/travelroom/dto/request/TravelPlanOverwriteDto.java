package com.example.travelday.domain.travelroom.dto.request;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TravelPlanOverwriteDto(

    @NotNull
    Long id,

    @Nullable
    int scheduledDay,

    @NotNull
    int position
) {
}
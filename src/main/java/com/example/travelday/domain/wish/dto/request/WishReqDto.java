package com.example.travelday.domain.wish.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WishReqDto(

        @NotBlank
        String name,

        @NotNull
        double latitude,

        @NotNull
        double longitude
) {
}
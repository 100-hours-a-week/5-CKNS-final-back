package com.example.travelday.domain.travelroom.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record WishReqDto(
        @NotBlank(message = "장소 이름이 없습니다.")
        String name,

        @NotBlank(message = "위도가 없습니다.")
        double latitude,

        @NotBlank(message = "경도가 없습니다.")
        double longitude
) {
}

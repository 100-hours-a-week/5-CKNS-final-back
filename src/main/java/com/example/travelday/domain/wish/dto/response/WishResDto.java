package com.example.travelday.domain.wish.dto.response;

import com.example.travelday.domain.wish.entity.Wish;
import lombok.Builder;

@Builder
public record WishResDto(
        Long wishId,
        String name,
        double latitude,
        double longitude
) {
    public static WishResDto of(Wish wish) {
        return WishResDto.builder()
                .wishId(wish.getId())
                .name(wish.getName())
                .latitude(wish.getLatitude())
                .longitude(wish.getLongitude())
                .build();
    }
}

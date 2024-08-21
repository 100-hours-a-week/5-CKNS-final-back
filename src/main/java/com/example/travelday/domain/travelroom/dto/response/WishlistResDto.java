package com.example.travelday.domain.travelroom.dto.response;

import com.example.travelday.domain.travelroom.entity.Wish;
import lombok.Builder;

@Builder
public record WishlistResDto(
        Long wishlistId,
        String name,
        double latitude,
        double longitude
) {
    public static WishlistResDto of(Wish wishlist) {
        return WishlistResDto.builder()
                .wishlistId(wishlist.getId())
                .name(wishlist.getName())
                .latitude(wishlist.getLatitude())
                .longitude(wishlist.getLongitude())
                .build();
    }
}

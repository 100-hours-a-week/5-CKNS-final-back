package com.example.travelday.domain.travelroom.service;

import com.example.travelday.domain.travelroom.dto.request.WishReqDto;
import com.example.travelday.domain.travelroom.dto.response.WishlistResDto;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.entity.Wish;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.WishRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;

    private final TravelRoomRepository travelRoomRepository;

    @Transactional(readOnly = true)
    public List<WishlistResDto> getWishlist(final Long travelRoomId) {
        List<Wish> wishList = wishRepository.findAllByTravelRoomId(travelRoomId);
        List<WishlistResDto> wishlistResDtos = new ArrayList<>();

        for (Wish wish : wishList) {
            wishlistResDtos.add(WishlistResDto.of(wish));
        }

        return wishlistResDtos;
    }

    @Transactional
    public void addWish(final Long travelRoomId, WishReqDto wishReqDto) {
        log.info("============= 서비스 ===============");
        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));
        log.info(String.valueOf(travelRoom));
        Wish wishlist = Wish.addOf(wishReqDto, travelRoom);
        wishRepository.save(wishlist);
    }

    @Transactional
    public void deleteWish(final Long wishId) {
        wishRepository.deleteById(wishId);
    }
}

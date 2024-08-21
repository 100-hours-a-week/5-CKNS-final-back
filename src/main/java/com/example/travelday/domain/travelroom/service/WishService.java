package com.example.travelday.domain.travelroom.service;

import com.example.travelday.domain.travelroom.dto.request.WishReqDto;
import com.example.travelday.domain.travelroom.dto.response.WishlistResDto;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.travelroom.entity.Wish;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.WishRepository;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishService {

    WishRepository wishRepository;

    TravelRoomRepository travelRoomRepository;

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
        TravelRoom travelRoom = travelRoomRepository.findRoomById(travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRAVELROOM_NOT_FOUND));

        Wish wishlist = Wish.addOf(wishReqDto, travelRoom);
        wishRepository.save(wishlist);
    }

    @Transactional
    public void deleteWish(final Long wishId) {
        wishRepository.deleteById(wishId);
    }
}

package com.example.travelday.domain.wish.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.domain.wish.dto.request.WishReqDto;
import com.example.travelday.domain.wish.dto.response.WishResDto;
import com.example.travelday.domain.travelroom.entity.TravelRoom;
import com.example.travelday.domain.wish.entity.Wish;
import com.example.travelday.domain.travelroom.repository.TravelRoomRepository;
import com.example.travelday.domain.travelroom.repository.UserTravelRoomRepository;
import com.example.travelday.domain.wish.repository.WishRepository;
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

    private final UserTravelRoomRepository userTravelRoomRepository;

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<WishResDto> getWishlist(final Long travelRoomId, String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        userTravelRoomRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_IN_TRAVEL_ROOM));

        List<Wish> wishList = wishRepository.findAllByTravelRoomId(travelRoomId);
        List<WishResDto> wishResDtos = new ArrayList<>();

        for (Wish wish : wishList) {
            wishResDtos.add(WishResDto.of(wish));
        }

        return wishResDtos;
    }

    @Transactional
    public void addWish(final Long travelRoomId, WishReqDto wishReqDto, String userId) {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        userTravelRoomRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_IN_TRAVEL_ROOM));

        TravelRoom travelRoom = travelRoomRepository.findById(travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.TRAVEL_ROOM_NOT_FOUND));

        Wish wish = Wish.addOf(wishReqDto, travelRoom);
        wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(final Long travelRoomId, final Long wishId, String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        userTravelRoomRepository.findByMemberAndTravelRoomId(member, travelRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_IN_TRAVEL_ROOM));

        wishRepository.deleteById(wishId);
    }
}

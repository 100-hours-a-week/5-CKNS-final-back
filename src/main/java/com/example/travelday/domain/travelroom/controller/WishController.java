package com.example.travelday.domain.travelroom.controller;

import com.example.travelday.domain.travelroom.dto.request.WishReqDto;
import com.example.travelday.domain.travelroom.dto.response.WishResDto;
import com.example.travelday.domain.travelroom.service.WishService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/rooms/{travelRoomId}/wishlist")
@RestController
public class WishController {

    private final WishService wishService;

    /**
     * 위시리스트 조회
     */
    @GetMapping()
    public ResponseEntity<ApiResponseEntity<List<WishResDto>>> getWishlist(@PathVariable Long travelRoomId) {
        return ResponseEntity.ok(ApiResponseEntity.of(wishService.getWishlist(travelRoomId)));
    }

    /**
     * 위시 추가
     */
    @PostMapping()
    public ResponseEntity<ApiResponseEntity<String>> addWish(@PathVariable Long travelRoomId, @Valid @RequestBody WishReqDto wishReqDto) {
        wishService.addWish(travelRoomId, wishReqDto);
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_ADD_WISH));
    }

    /**
     * 위시 삭제
     */
    @DeleteMapping("/{wishId}")
    public ResponseEntity<ApiResponseEntity<String>> deleteWish(@PathVariable Long wishId) {
        wishService.deleteWish(wishId);
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_DELETE_WISH));
    }
}

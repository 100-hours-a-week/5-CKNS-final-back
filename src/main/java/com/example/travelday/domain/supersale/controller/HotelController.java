package com.example.travelday.domain.supersale.controller;

import com.example.travelday.domain.supersale.dto.response.HotelResDto;
import com.example.travelday.domain.supersale.service.HotelService;
import com.example.travelday.global.common.ApiResponseEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    /**
     * 호텔 조회 API
     */
    @GetMapping()
    public ResponseEntity<ApiResponseEntity<List<HotelResDto>>> getHotelsByGeocode(@RequestParam double latitude, @RequestParam double longitude) {
        return ResponseEntity.ok(ApiResponseEntity.of(hotelService.getHotelsByGeocode(latitude, longitude)));
    }
}

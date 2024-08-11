package com.example.travelday.domain.hotel.controller;

import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    @GetMapping
    public ResponseEntity<ApiResponseEntity<String>> get() {
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.OK));
    }
}

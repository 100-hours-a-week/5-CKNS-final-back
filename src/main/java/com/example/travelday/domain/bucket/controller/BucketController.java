package com.example.travelday.domain.bucket.controller;

import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.utils.BucketUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
public class BucketController {

    private final BucketUtils bucketUtils;

    @GetMapping("/bucketState")
    public ResponseEntity<ApiResponseEntity<String>> getBucketStatus() {
        long availableTokens = bucketUtils.getAvailableTokens();
        return ResponseEntity.ok(ApiResponseEntity.of("남은 버킷 토큰 수: " + availableTokens));
    }

}

package com.example.travelday.domain.fcm.controller;

import com.example.travelday.domain.fcm.dto.request.FcmCreateReqDto;
import com.example.travelday.domain.fcm.service.FcmService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PostMapping()
    public ResponseEntity<ApiResponseEntity<String>> createFcmToken(
            @RequestBody FcmCreateReqDto fcmCreateReqDto,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
//        fcmService.createFcmToken(userDetails.getUsername(), fcmCreateReqDto.fcmToken());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_CREATE_FCM_TOKEN));
    }
}

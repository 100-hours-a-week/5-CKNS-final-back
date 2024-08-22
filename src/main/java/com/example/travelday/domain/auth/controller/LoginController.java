package com.example.travelday.domain.auth.controller;

import com.example.travelday.domain.auth.service.LoginService;
import com.example.travelday.domain.auth.service.TokenRefreshService;
import com.example.travelday.global.auth.jwt.dto.AccessTokenDto;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final TokenRefreshService tokenRefreshService;

    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseEntity<AccessTokenDto>> refresh(HttpServletRequest request) {
        // Access Token 갱신
        AccessTokenDto accessTokenDto = tokenRefreshService.userRefresh(request);

        return ResponseEntity.ok(ApiResponseEntity.of(accessTokenDto));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseEntity<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        // 로그아웃
        loginService.logout(request, response);

        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_LOGOUT));
    }
}
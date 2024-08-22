package com.example.travelday.domain.auth.service;

import com.example.travelday.domain.auth.enums.TokenName;
import com.example.travelday.global.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

    private final StringRedisTemplate redisTemplate;

    /**
     * 로그아웃
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Refresh Token 쿠키 조회
        final String refreshToken = CookieUtils.getCookieValue(request.getCookies(), TokenName.USER_REFRESH_TOKEN.name());

        // Redis에 저장된 Refresh Token 삭제
        redisTemplate.delete(refreshToken);

        // Refresh Token 쿠키 삭제
        Cookie cookie = CookieUtils.getCookieForRemove(TokenName.USER_REFRESH_TOKEN.name());
        response.addCookie(cookie);
    }
}

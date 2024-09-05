package com.example.travelday.domain.auth.service;

import com.example.travelday.domain.auth.enums.Role;
import com.example.travelday.domain.auth.enums.TokenName;
import com.example.travelday.global.auth.jwt.component.JwtTokenProvider;
import com.example.travelday.global.auth.jwt.dto.AccessTokenDto;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.example.travelday.global.utils.CookieUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final StringRedisTemplate redisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    public AccessTokenDto userRefresh(HttpServletRequest request) {
        final String refreshToken = CookieUtils.getCookieValue(request.getCookies(), TokenName.USER_REFRESH_TOKEN.name());
        return refresh(refreshToken, Role.ROLE_USER);
    }

    /**
     * Refresh Token으로 Access Token 갱신
     */
    private AccessTokenDto refresh(final String refreshToken, Role role) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // Redis에서 Refresh Token으로 ID 가져오기
        String userName = redisTemplate.opsForValue().get(refreshToken);
        if (StringUtils.isBlank(userName)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(userName, "", List.of(role::name));

        // 인증 정보를 기반으로 JWT Token 생성
        return jwtTokenProvider.generateAccessToken(authentication);
    }
}

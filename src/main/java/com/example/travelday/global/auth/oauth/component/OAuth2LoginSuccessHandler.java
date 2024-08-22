package com.example.travelday.global.auth.oauth.component;

import com.example.travelday.domain.auth.enums.TokenName;
import com.example.travelday.global.auth.jwt.component.JwtTokenProvider;
import com.example.travelday.global.auth.jwt.dto.RefreshTokenDto;
import com.example.travelday.global.auth.jwt.dto.TokenDto;
import com.example.travelday.global.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final StringRedisTemplate redisTemplate;

    @Value("${oauth2.redirect-url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        RefreshTokenDto refreshTokenDto = tokenDto.refreshTokenDto();

        // Refresh Token을 Redis에 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshTokenDto.token(), authentication.getName(), refreshTokenDto.getExpiresInSecond(), TimeUnit.SECONDS);

        // Refresh Token을 쿠키에 담아서 전달
        Cookie cookie = CookieUtils.createCookie(TokenName.USER_REFRESH_TOKEN.name(), refreshTokenDto.token(), refreshTokenDto.getExpiresInSecond());
        response.addCookie(cookie);

        response.sendRedirect(redirectUrl + "/login/oauth2/success?accessToken=" + tokenDto.accessTokenDto().token());
    }
}

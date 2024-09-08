package com.example.travelday.global.auth.jwt.component;

import com.example.travelday.global.auth.jwt.dto.AccessTokenDto;
import com.example.travelday.global.auth.jwt.dto.RefreshTokenDto;
import com.example.travelday.global.auth.jwt.dto.TokenDto;
import com.example.travelday.global.exception.ExpiredTokenException;
import com.example.travelday.global.exception.InvalidTokenException;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private String accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private String refreshTokenExpiration;

    /**
     * Access Token과 Refresh Token을 생성하는 메소드
     */
    public TokenDto generateToken(Authentication authentication) {
        AccessTokenDto accessTokenDto = generateAccessToken(authentication);
        RefreshTokenDto refreshTokenDto = generateRefreshToken();

        return TokenDto.builder()
                .accessTokenDto(accessTokenDto)
                .refreshTokenDto(refreshTokenDto)
                .build();
    }

    /**
     * Access Token을 생성하는 메소드
     */
    public AccessTokenDto generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 만료 시간 설정
        Date accessTokenExpiresIn = new Date(now + 1000 * Long.parseLong(accessTokenExpiration));

        // Access Token 생성
        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("auth", authorities)
                .expiration(accessTokenExpiresIn)
                .signWith(getSigningKey(), SIG.HS256)
                .compact();

        return AccessTokenDto.builder()
                .token(accessToken)
                .expiresIn(accessTokenExpiresIn.getTime())
                .build();
    }

    /**
     * Refresh Token을 생성하는 메소드
     */
    private RefreshTokenDto generateRefreshToken() {
        long now = (new Date()).getTime();

        // Refresh Token 만료 시간 설정
        Date refreshTokenExpiresIn = new Date(now + 1000 * Long.parseLong(refreshTokenExpiration));

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .expiration(refreshTokenExpiresIn)
                .signWith(getSigningKey(), SIG.HS256)
                .compact();

        return RefreshTokenDto.builder()
                .token(refreshToken)
                .expiresIn(refreshTokenExpiresIn.getTime())
                .build();
    }

    /**
     * Access Token을 파싱하여 Authentication 객체를 반환하는 메소드
     */
    public Authentication getAuthentication(final String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        // 권한정보 획득
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * AccessToken의 유효성을 검증하는 메소드
     */
    public boolean validateToken(final String accessToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(accessToken);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            throw new InvalidTokenException(ErrorCode.INVALID_ACCESS_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage());
        }
    }

    /**
     * SecretKey를 생성하는 메소드
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * AccessToken을 파싱하여 Claims를 반환하는 메소드
     *
     * @param accessToken AccessToken
     * @return Claims
     */
    private Claims parseClaims(final String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        }
    }
}

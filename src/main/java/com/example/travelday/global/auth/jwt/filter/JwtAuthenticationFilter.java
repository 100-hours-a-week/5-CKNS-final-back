package com.example.travelday.global.auth.jwt.filter;

import com.example.travelday.global.auth.jwt.component.JwtTokenProvider;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import com.example.travelday.global.exception.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token = resolveToken(request);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            if (e.getMessage().equals(ErrorCode.EXPIRED_ACCESS_TOKEN.getMessage())) {
                setResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
            } else if (e.getMessage().equals(ErrorCode.INVALID_ACCESS_TOKEN.getMessage())) {
                setResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
            }
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("status", code.getHttpStatus());
        responseJson.put("message", code.getMessage());
        responseJson.put("code", code.getCode());

        response.getWriter().print(responseJson);
    }

    /**
     * Request Header에서 Token을 추출하는 메소드
     */
    private String resolveToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}

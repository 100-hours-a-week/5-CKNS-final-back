package com.example.travelday.domain.chat.utils;

import com.example.travelday.global.auth.jwt.component.JwtProperties;
import com.example.travelday.global.auth.jwt.component.JwtTokenProvider;
import com.example.travelday.global.exception.TokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
// spring security filter chain 이전에 실행되도록 우선순위 설정
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JwtProperties jwtProperties;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert headerAccessor != null;

        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 시에만 header 확인
            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
            log.info("Token: {}", token);
            token = token.replace(jwtProperties.TOKEN_PREFIX, "").trim();
            log.info("Token replace prefix: {}", token);

            try {
                if (jwtTokenProvider.validateToken(token)) { // 토큰 검증
                    String userId = jwtTokenProvider.getAuthentication(token).getName(); // Authentication에서 userId 추출
                    headerAccessor.addNativeHeader("UserId", userId); // Stomp 헤더에 추가
                }
            } catch (TokenException e) {
                log.error("Token validation failed: {}", e.getMessage());
                throw new IllegalStateException("Invalid or expired token.");
            }
        }
        return message;
    }
}

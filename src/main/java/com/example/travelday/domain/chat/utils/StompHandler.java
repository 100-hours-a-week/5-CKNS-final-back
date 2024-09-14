package com.example.travelday.domain.chat.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.travelday.global.auth.jwt.component.JwtProperties;
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

    @Override
    // 메세지가 채널로 전송되기 전에 호출되는 메서드
    // jwt 토큰을 통해 사용자 정보를 가져와서 stomp 헤더에 추가
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // stompheaderAccesor를 통해 stomp헤더에 접근할 수 있다.
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert headerAccessor != null;

        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 시에만 header 확인
            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
            token = token.replace(JwtProperties.TOKEN_PREFIX, "");

            try {
                Integer userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY))
                                        .build()
                                        .verify(token)
                                        .getClaim("userId")
                                        .asInt();

                headerAccessor.addNativeHeader("UserId", String.valueOf(userId));
            } catch (TokenExpiredException e) {
                log.error("Token is expired");
                throw new IllegalStateException("Token is expired");
            } catch (Exception e) {
                log.error("Token is invalid");
                throw new IllegalStateException("Token is invalid");
            }
        }
        return message;
    }
}

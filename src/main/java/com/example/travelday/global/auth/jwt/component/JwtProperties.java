package com.example.travelday.global.auth.jwt.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${websocket.jwt.token-prefix}")
    public String TOKEN_PREFIX;

    @Value("${websocket.jwt.header-string}")
    public String HEADER_STRING;
}

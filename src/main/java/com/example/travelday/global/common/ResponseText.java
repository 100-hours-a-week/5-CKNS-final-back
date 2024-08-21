package com.example.travelday.global.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지
public class ResponseText {

    public static final String OK = "OK";

    public static final String SUCCESS_CREATE_TRAVELROOM = "여행방 생성 성공";
}

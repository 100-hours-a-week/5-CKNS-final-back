package com.example.travelday.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 계정 관련 에러코드
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AU001", "만료된 엑세스 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AU002", "유효하지 않은 엑세스 토큰입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AU003", "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AU004", "로그인이 필요합니다."),
    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "AU005", "잘못된 소셜로그인 타입입니다."),

    // 호텔 관련 오류
    FAIL_TO_GET_HOTEL_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "HT001", "호텔 정보를 가져오는데 실패하였습니다."),

    // 항공권 관련 오류
    FAIL_TO_GET_FLIGHT_INFO(HttpStatus.INTERNAL_SERVER_ERROR,"FL001", "항공권 오류"),

    // 서버 오류
    SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SV001", "서버 오류");


    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}

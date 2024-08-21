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

    // hotel error
    FAIL_TO_GET_HOTEL_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "HT001", "호텔 정보를 가져오는데 실패하였습니다."),

    // flight error
    FAIL_TO_GET_FLIGHT_INFO(HttpStatus.INTERNAL_SERVER_ERROR,"FL001", "항공권 오류"),

    // travel room error
    TRAVELROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "TR001", "존재하지 않는 여행방입니다."),

    // 서버 오류
    // travelRoom error,
    TRAVEL_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "TR001", "여행방을 찾을 수 없습니다."),

    // user error
    NICKNAME_ALREADY_TAKEN(HttpStatus.CONFLICT, "NICKNAME-001", "이미 사용 중인 닉네임입니다."),
    NICKNAME_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "NICKNAME-005", "닉네임 생성에 실패했습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "TR001", "존재하지 않는 회원입니다."),

    //notification error
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION-001", "알림을 찾을 수 없습니다."),
    NOTIFICATION_ALREADY_READ(HttpStatus.BAD_REQUEST, "NOTIFICATION-002", "이미 읽은 알림입니다."),
    NOTIFICATION_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "NOTIFICATION-003", "알림 전송에 실패했습니다."),
    NOTIFICATION_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "NOTIFICATION-004", "알림 서비스가 이용 불가능합니다."),
    INVALID_NOTIFICATION_PAYLOAD(HttpStatus.BAD_REQUEST, "NOTIFICATION-005", "알림 내용이 유효하지 않습니다."),

    // server error
    SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SV001", "서버 오류");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}

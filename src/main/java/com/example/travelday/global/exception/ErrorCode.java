package com.example.travelday.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // auth error
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AU001", "만료된 엑세스 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AU002", "유효하지 않은 엑세스 토큰입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AU003", "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AU004", "로그인이 필요합니다."),
    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "AU005", "잘못된 소셜로그인 타입입니다."),

    // hotel error
    FAIL_TO_GET_HOTEL_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "HT001", "호텔 정보를 가져오는데 실패하였습니다."),

    // flight error
    FAIL_TO_GET_FLIGHT_INFO(HttpStatus.INTERNAL_SERVER_ERROR,"FL001", "항공권 오류입니다."),
    FLIGHT_NOT_FOUND(HttpStatus.BAD_REQUEST, "FL002", "존재하지 않는 항공권입니다."),
    AIRPORT_NOT_FOUND(HttpStatus.BAD_REQUEST, "FL003", "존재하지 않는 공항입니다."),

    // travel room error
    TRAVEL_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "TR001", "존재하지 않는 여행방입니다."),
    USER_NOT_IN_TRAVEL_ROOM(HttpStatus.BAD_REQUEST, "TR002", "해당 여행방에 없는 회원입니다."),
    USER_DOES_NOT_JOIN_TRAVEL_ROOM(HttpStatus.FORBIDDEN, "TR003", "해당 회원이 속해있는 여행방이 없습니다."),
    TRAVEL_ROOM_CLOSED(HttpStatus.BAD_REQUEST, "TR004", "여행방 여행날짜가 지났습니다."),

    // travel plan error
    TRAVEL_PLAN_NOT_FOUND(HttpStatus.BAD_REQUEST, "TP001", "존재하지 않는 여행 일정입니다."),

    // settlement error
    SETTLEMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "ST001", "존재하지 않는 정산입니다."),
    SETTLEMENT_DETAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "ST002", "존재하지 않는 정산 내역입니다."),
    INVALID_AMOUNT_RANGE(HttpStatus.BAD_REQUEST, "ST003", "금액 범위가 유효하지 않습니다."),

    // user error
    NICKNAME_ALREADY_TAKEN(HttpStatus.CONFLICT, "MB001", "이미 사용 중인 닉네임입니다."),
    NICKNAME_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MB002", "닉네임 생성에 실패했습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MB003", "존재하지 않는 회원입니다."),

    // notification error
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NT001", "알림을 찾을 수 없습니다."),
    NOTIFICATION_ALREADY_READ(HttpStatus.BAD_REQUEST, "NT002", "이미 읽은 알림입니다."),
    NOTIFICATION_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "NT003", "알림 전송에 실패했습니다."),
    NOTIFICATION_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "NT004", "알림 서비스가 이용 불가능합니다."),
    INVALID_NOTIFICATION_PAYLOAD(HttpStatus.BAD_REQUEST, "NT005", "알림 내용이 유효하지 않습니다."),
    FCM_TOKEN_DUPLICATE(HttpStatus.BAD_REQUEST, "NT006", "중복된 FCM 토큰입니다."),
    FCM_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "NT007", "FCM 토큰을 찾을 수 없습니다."),

    // invitation error
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "IV001", "초대장을 찾을 수 없습니다."),
    BAD_REQUEST_FLAG(HttpStatus.BAD_REQUEST, "IV002", "초대장 수락 여부 요청 형식이 잘못되었습니다."),
    ALREADY_IN_TRAVELROOM(HttpStatus.BAD_REQUEST, "IV003", "이미 여행방에 있는 회원입니다."),
    ALREADY_SEND_INVITATION(HttpStatus.BAD_REQUEST, "IV004", "이미 초대장을 보냈습니다."),
    CANNOT_INVITE_SELF(HttpStatus.BAD_REQUEST, "IV005", "자기 자신을 초대할 수 없습니다."),
    INVALID_INVITATION_STATUS(HttpStatus.BAD_REQUEST, "IV006", "유효하지 않은 초대장 상태입니다."),

    // wish error
    WISH_NOT_FOUND(HttpStatus.BAD_REQUEST, "WH001", "위시를 찾을 수 없습니다."),

    // chat error
    CHAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CH001", "채팅이 존재하지 않습니다."),

    // redis error
    REDIS_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RE001", "Redis 저장에 실패했습니다."),

    // server error
    SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SV001", "서버 오류입니다."),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "SV002", "요청이 너무 많습니다.");

    private final HttpStatus httpStatus;

    private final String code;

    private final String message;
}

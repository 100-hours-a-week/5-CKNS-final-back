package com.example.travelday.global.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지
public class ResponseText {

    // common
    public static final String OK = "OK";
    public static final String DUPLICATE = "DUPLICATE";

    // wish
    public static final String SUCCESS_ADD_WISH = "위시 추가 성공";
    public static final String SUCCESS_DELETE_WISH = "위시 삭제 성공";

    // travelroom
    public static final String SUCCESS_CREATE_TRAVELROOM = "여행방 생성 성공";
    public static final String SUCCESS_UPDATE_TRAVELROOM = "여행방 수정 성공";
    public static final String SUCCESS_DELETE_TRAVELROOM = "여행 일정 목록 삭제 성공";

    // member
    public static final String SUCCESS_LOGOUT = "로그아웃 성공";
    public static final String SUCCESS_UPDATE_NICKNAME = "닉네임 수정 성공";

    // travelplan
    public static final String SUCCESS_CREATE_TRAVELPLAN = "여행 일정 생성 성공";
    public static final String SUCCESS_UPDATE_TRAVELPLAN = "여행 일정 갱신 성공";
    public static final String SUCCESS_CREATE_TRAVELPLANLIST = "여행 일정 목록 생성 성공";
    public static final String SUCCESS_DELETE_TRAVELPLAN = "여행일정 삭제 성공";

    // settlement
    public static final String SUCCESS_ADD_SETTLEMENTDETAIL = "정산 내역 추가 성공";
    public static final String SUCCESS_UPDATE_SETTLEMENTDETAIL = "정산 내역 수정 성공";
    public static final String SUCCESS_CREATE_SETTLEMENT = "정산 생성 성공";
    public static final String SUCCESS_DELETE_SETTLEMENT = "정산 삭제 성공";
    public static final String SUCCESS_DELETE_SETTLEMENTDETAIL = "정산 내역 삭제 성공";
    public static final String SUCCESS_NOTIFY_SETTLEMENTDETAIL = "정산 내역 상태 변경 성공";

  // invitation
    public static final String SUCCESS_CREATE_INVITATION = "여행방 초대 성공";
    public static final String SUCCESS_CREATE_FCM_TOKEN = "FCM 토큰 생성 성공";
    public static final String SUCCESS_INVITATION_RESPONSE = "초대 응답 성공";
}

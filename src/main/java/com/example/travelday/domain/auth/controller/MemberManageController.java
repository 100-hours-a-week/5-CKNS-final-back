package com.example.travelday.domain.auth.controller;

import com.example.travelday.domain.auth.dto.request.UpdateNicknameReqDto;

import com.example.travelday.domain.auth.service.FileService;

import com.example.travelday.domain.auth.entity.Member;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.travelday.domain.auth.dto.response.MemberInfoResDto;
import com.example.travelday.domain.auth.service.MemberManageService;
import com.example.travelday.global.common.ApiResponseEntity;
import com.example.travelday.global.common.ResponseText;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class MemberManageController {

    private final MemberManageService memberManageService;

    private final FileService fileService;

    /**
     * 회원 정보 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponseEntity<MemberInfoResDto>> getMemberInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                ApiResponseEntity.of(memberManageService.getInfo(userDetails.getUsername())));
    }

    /**
     * 닉네임 중복 검사
     */
    @GetMapping("/nickname/check")
    public ResponseEntity<ApiResponseEntity<String>> duplicateNickname(
            @RequestParam String nickname) {
        boolean isDuplicate = memberManageService.checkDuplicateNickname(nickname);
        return ResponseEntity.ok(
                ApiResponseEntity.of(isDuplicate ? ResponseText.DUPLICATE : ResponseText.OK));
    }

    /**
     * 닉네임 수정
     */
    @PutMapping("/nickname")
    public ResponseEntity<ApiResponseEntity<String>> updateNickname(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateNicknameReqDto reqDto) {
        memberManageService.updateNickname(userDetails.getUsername(), reqDto.nickname());
        return ResponseEntity.ok(ApiResponseEntity.of(ResponseText.SUCCESS_UPDATE_NICKNAME));
    }

    /**
     * 프로필 이미지 등록 URL 요청
     * */
    @GetMapping("/profile/preSignedUrl/{filename}")
    public ResponseEntity<ApiResponseEntity<String>> getPreSignedUrl(@AuthenticationPrincipal UserDetails userDetails, @PathVariable(value = "filename") String fileName) {

        String uniqueName = fileService.getFileName("image", fileName);
        // 프로필 이미지 경로를 멤버 컬럼에 저장
        String userId = userDetails.getUsername();
        memberManageService.updateProfileImagePath(userId, uniqueName);

        return ResponseEntity.ok(ApiResponseEntity.of(fileService.getPreSignedUrl(uniqueName)));
    }
}

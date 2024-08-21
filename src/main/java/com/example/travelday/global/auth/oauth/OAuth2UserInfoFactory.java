package com.example.travelday.global.auth.oauth;

import com.example.travelday.global.auth.oauth.data.KakaoOAuth2UserInfo;
import com.example.travelday.global.auth.oauth.data.OAuth2UserInfo;
import com.example.travelday.global.auth.oauth.enums.SocialType;
import com.example.travelday.global.exception.CustomException;
import com.example.travelday.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class OAuth2UserInfoFactory {

    private final OAuth2UserInfo oAuth2UserInfo;

    public static OAuth2UserInfo of(SocialType socialType, Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return new KakaoOAuth2UserInfo(attributes);
        } else {
            throw new CustomException(ErrorCode.INVALID_SOCIAL_TYPE);
        }
    }
}

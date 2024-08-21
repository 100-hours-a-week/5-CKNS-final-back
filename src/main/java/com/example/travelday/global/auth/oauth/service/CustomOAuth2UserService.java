package com.example.travelday.global.auth.oauth.service;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.auth.repository.MemberRepository;
import com.example.travelday.global.auth.oauth.CustomOAuth2User;
import com.example.travelday.global.auth.oauth.OAuth2UserInfoFactory;
import com.example.travelday.global.auth.oauth.data.OAuth2UserInfo;
import com.example.travelday.global.auth.oauth.enums.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        log.info("===== check1 =======");

        final String registrationId = userRequest.getClientRegistration().getRegistrationId();
        final String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        final SocialType socialType = getSocialType(registrationId);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.of(getSocialType(registrationId), attributes);

        log.info("===== check2 =======");

        // 이미 가입된 회원인지 확인 후 가입되지 않은 회원이면 저장
        Optional<Member> member = memberRepository.findByUserIdAndSocialType(oAuth2UserInfo.getId(), socialType);
        if (member.isEmpty()) {
            saveMember(oAuth2UserInfo, socialType);
        }

        return new CustomOAuth2User(
                oAuth2User.getAttributes(),
                userNameAttributeName,
                oAuth2UserInfo.getId()
        );
    }

    /**
     * SocialType 반환
     */
    private SocialType getSocialType(final String registrationId) {
        return SocialType.valueOf(registrationId.toUpperCase());
    }

    /**
     * 회원 저장
     */
    private void saveMember(OAuth2UserInfo oAuth2UserInfo, SocialType socialType) {
        Member member = Member.builder()
                .userId(oAuth2UserInfo.getId())
                .nickname("user" + UUID.randomUUID().toString().replace("-", "").substring(16))
                .socialType(socialType)
                .updatedBy(oAuth2UserInfo.getId())
                .build();
        memberRepository.save(member);
    }
}

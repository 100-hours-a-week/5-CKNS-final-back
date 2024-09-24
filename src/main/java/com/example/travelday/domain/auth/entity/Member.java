package com.example.travelday.domain.auth.entity;

import com.example.travelday.global.auth.oauth.enums.SocialType;
import com.example.travelday.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Comment("사용자 계정 정보")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "id", length = 50, nullable = false, updatable = false)
    @Comment("아이디")
    private String userId;

    @Size(max = 30, message = "최대 30자리까지 입력 가능합니다.")
    @Column(name = "nickname", length = 20, nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Column(name = "social_type", updatable = false)
    @Comment("SNS 로그인 유형(KAKAO)")
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "DATETIME")
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Comment("등록일자")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Comment("수정일자")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "upd_id", nullable = false)
    @Comment("수정자")
    private String updatedBy;

    @Column(name = "profile_image_path", length = 255)
    @Comment("프로필 이미지 경로")
    private String profileImagePath;

    @Builder
    public Member(String userId, String nickname, SocialType socialType, String updatedBy, String profileImagePath) {
        this.userId = userId;
        this.nickname = nickname;
        this.socialType = socialType;
        this.updatedBy = updatedBy;
        this.profileImagePath = profileImagePath;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }
}

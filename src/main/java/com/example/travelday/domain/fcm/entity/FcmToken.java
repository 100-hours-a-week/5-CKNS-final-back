package com.example.travelday.domain.fcm.entity;

import com.example.travelday.domain.auth.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "fcm_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fcm_token", nullable = false)
    private String token;

    @LastModifiedDate
    @Column(name = "last_used_time", nullable = false)
    private LocalDateTime lastUsedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public FcmToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public void updateToken(String newToken) {
        this.token = newToken;
    }

    public void updateLastUsedTime() {
        this.lastUsedTime = LocalDateTime.now();
    }
}

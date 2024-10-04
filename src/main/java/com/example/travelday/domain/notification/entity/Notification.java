package com.example.travelday.domain.notification.entity;

import com.example.travelday.domain.auth.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "travelroom_id", nullable = false)
    private Long travelRoomId;

    @CreatedDate
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;

    @Builder
    public Notification(Member member, String content, Long travelRoomId) {
        this.member = member;
        this.content = content;
        this.travelRoomId = travelRoomId;
        this.isChecked = false;
    }

    public void check() {
        this.isChecked = true;
    }
}

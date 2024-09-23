package com.example.travelday.domain.notification.repository;

import com.example.travelday.domain.auth.entity.Member;
import com.example.travelday.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByMemberAndIsCheckedFalseOrderByCreatedTimeDesc(Member member);

    Optional<Notification> findByMemberAndTravelRoomId(Member member, Long travelroomId);
}

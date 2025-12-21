package com.myce.notification.repository;

import com.myce.notification.document.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByMemberId(Long memberId, Sort sort);

}

package com.hashedin.stockservice.repository;

import com.hashedin.stockservice.model.Notification;
import com.hashedin.stockservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findNotificationByUsername(String username);
}

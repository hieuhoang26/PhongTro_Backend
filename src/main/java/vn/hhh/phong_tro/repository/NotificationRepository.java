package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hhh.phong_tro.model.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}

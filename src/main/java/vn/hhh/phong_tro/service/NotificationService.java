package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.response.NotificationDto;
import vn.hhh.phong_tro.dto.response.PageResponse;
import vn.hhh.phong_tro.model.Notification;
import vn.hhh.phong_tro.repository.NotificationRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long userId, String type, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(false);
        notification.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
    }

    public PageResponse getNotificationsForUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notificationPage = notificationRepository.findByUserId(userId, pageable);

        List<NotificationDto> list = notificationPage.stream().map(notification -> {
            NotificationDto dto = new NotificationDto();
            dto.setId(notification.getId());
            dto.setUserId(notification.getUserId());
            dto.setType(notification.getType());
            dto.setTitle(notification.getTitle());
            dto.setContent(notification.getContent());
            dto.setIsRead(notification.getIsRead());
            dto.setCreatedAt(notification.getCreatedAt());
            return dto;
        }).toList();
        return  PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .total(notificationPage.getTotalPages())
                .items(list)
                .build();
    }

    public Long markRead(Long id){
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return notification.getId();
    }
    public void markAllRead(Long userId){
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
    }
}

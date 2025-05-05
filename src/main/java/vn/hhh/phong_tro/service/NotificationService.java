package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.chat.ChatMessage;
import vn.hhh.phong_tro.model.Notification;
import vn.hhh.phong_tro.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {


    private final NotificationRepository notificationRepository;

//    public Notification createMessageNotification(ChatMessage dto) {
//        Notification noti = new Notification();
//        noti.setUserId(dto.getReceiverId());
//        noti.setType("message");
//        noti.setTitle("New Message");
//        noti.setContent("You have a new message from user " + dto.getSenderId());
////        noti.setTargetUrl("/chat/" + dto.getSenderId());
//        return notificationRepository.save(noti);
//    }

//    public List<ChatMessage> getNotificationsForUser(Long userId) {
//        List<Notification> list =  notificationRepository.findByUserId(userId);
//        return list.stream()
//                .map(message -> {
//                    ChatMessage dto = new ChatMessage();
//                    dto.setReceiverId(message.getUserId());
//                    dto.setContent(message.getContent());
//                    return dto;
//                })
//                .toList();
//    }
}

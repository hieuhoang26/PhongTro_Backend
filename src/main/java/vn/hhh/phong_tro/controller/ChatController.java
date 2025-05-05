package vn.hhh.phong_tro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.hhh.phong_tro.dto.chat.ChatMessage;
import vn.hhh.phong_tro.dto.chat.UserChatDto;
import vn.hhh.phong_tro.model.Message;
import vn.hhh.phong_tro.service.MessageService;
import vn.hhh.phong_tro.service.NotificationService;
import vn.hhh.phong_tro.util.Uri;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(Uri.Chat)
@Slf4j
public class ChatController {

    private final NotificationService notificationService;

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage dto, Principal principal) {
        // 1. Save message
        Message message = messageService.saveMessage(dto);
        // 2. Send to receiver's WebSocket topic
        messagingTemplate.convertAndSendToUser(
                dto.getReceiverId(),
                "/queue/messages",
                message
        );
    }

    @GetMapping("/conversation/{userA}/{userB}")
    public ResponseEntity<List<ChatMessage>> getConversation(@PathVariable String userA, @PathVariable String userB) {
        return ResponseEntity.ok(messageService.getConversation(userA, userB));
    }
    @GetMapping("/contacts/{userId}")
    public ResponseEntity<List<UserChatDto>> getUserContacts(
            @PathVariable Long userId) {
        List<UserChatDto> contacts = messageService.getContacts(userId);
        return ResponseEntity.ok(contacts);
    }
}


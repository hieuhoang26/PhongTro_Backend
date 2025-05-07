package vn.hhh.phong_tro.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.hhh.phong_tro.dto.chat.ChatMessageRequest;
import vn.hhh.phong_tro.dto.chat.ConversationDTO;
import vn.hhh.phong_tro.dto.chat.MessageDTO;
import vn.hhh.phong_tro.dto.chat.UserChatDto;
import vn.hhh.phong_tro.model.Conversation;
import vn.hhh.phong_tro.model.Message;
import vn.hhh.phong_tro.service.ChatService;
import vn.hhh.phong_tro.service.NotificationService;
import vn.hhh.phong_tro.util.Uri;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(Uri.Chat)
@Slf4j
public class ChatController {

    private final NotificationService notificationService;

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatService chatService;

    // Gửi tin nhắn
    @MessageMapping("/chat.send") // /app/chat.send
    public void sendMessage(@Payload ChatMessageRequest messageDTO) {
        Message message = chatService.saveMessage(
                messageDTO.getSenderId(),
                messageDTO.getReceiverId(),
                messageDTO.getContent(),
                messageDTO.getContentType()
        );

        Long conversationId = message.getConversation().getId();
        // Gửi về cho người nhận
        messagingTemplate.convertAndSend("/topic/conversations-" + conversationId, message);
    }
    @Operation(summary = "searchUserforChat ", description = "")
    @GetMapping("/search")
    public ResponseEntity<UserChatDto> searchUserforChat(
            @RequestParam String phoneOrName) {
        return ResponseEntity.ok(chatService.getSearchUser(phoneOrName));

    }
    @Operation(summary = "create or get conversion of user ", description = "Return list ")
    @GetMapping("/conversation")
    public ResponseEntity<ConversationDTO> getConversationsBetween(
            @RequestParam Integer userA,@RequestParam Integer userB) {
        ConversationDTO conversations = chatService.getOrCreateConversationDTO(Long.valueOf(userA),Long.valueOf(userB));
        return ResponseEntity.ok(conversations);

    }
    @Operation(summary = "List conversion of user ", description = "Return list ")
    @GetMapping("/conversation/list")
    public ResponseEntity<List<ConversationDTO>> getUserConversations(
            @RequestParam Long userId) {
            List<ConversationDTO> conversations = chatService.getUserConversations(userId);
            return ResponseEntity.ok(conversations);

    }
    @Operation(summary = "Get messages of conversation", description = "Return list of messages in a conversation")
    @GetMapping("/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestParam Long conversationId) {
        List<MessageDTO> messages = chatService.getMessages(conversationId);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build(); // trả về 204 No Content
        }
        return ResponseEntity.ok(messages);
    }




//    private final MessageService messageService;

//    @MessageMapping("/chat.send")
//    public void sendMessage(@Payload ChatMessage dto, Principal principal) {
//        // 1. Save message
//        Message message = messageService.saveMessage(dto);
//        log.info("message" + message);
//        // 2. Send to receiver's WebSocket topic
//        messagingTemplate.convertAndSendToUser(
//                dto.getReceiverId(),
//                "/queue/messages",
//                message
//        );
//        log.info("ReceiverId" +   dto.getReceiverId());
//    }

//    @MessageMapping("/chat/send-by-topic")
//    public void sendMessageByTopic(@Payload ChatMessage dto, Principal principal) {
//        // 1. Save message
//        Message message = messageService.saveMessage(dto);
//        log.info("message" + message);
//        // 2. Send to receiver's WebSocket topic
//        messagingTemplate.convertAndSend(
//                "/topic/conversation-1",
//                message
//        );
//        log.info("ReceiverId" +   dto.getReceiverId());
//    }




}


package vn.hhh.phong_tro.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.chat.ConversationDTO;
import vn.hhh.phong_tro.dto.chat.MessageDTO;
import vn.hhh.phong_tro.dto.chat.UserChatDto;
import vn.hhh.phong_tro.model.Conversation;
import vn.hhh.phong_tro.model.Message;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.repository.ConversationRepository;
import vn.hhh.phong_tro.repository.MessageRepository;
import vn.hhh.phong_tro.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

     private final ConversationRepository conversationRepo;
    private final MessageRepository messageRepo;
     private final UserRepository userRepo; // nếu cần

    public Message saveMessage(Integer senderId, Integer receiverId, String content, String contentType) {
        Conversation conversation = getOrCreateConversation(Long.valueOf(senderId), Long.valueOf(receiverId));
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(userRepo.findById(Long.valueOf(senderId)).orElseThrow());
        message.setContent(content);
        message.setContentType(contentType);
        return messageRepo.save(message);
    }

    @Transactional
    public List<MessageDTO> getMessages(Long conversationId) {
        List<Message> list = messageRepo.findByConversationIdOrderBySentAtAsc(conversationId);
        return list.stream()
                .map(this::mapMessageToDTO)
                .collect(Collectors.toList());
    }



    public Conversation getOrCreateConversation(Long senderId, Long receiverId) {
        User sender = userRepo.findById(senderId).orElseThrow();
        User receiver = userRepo.findById(receiverId).orElseThrow();
        return conversationRepo
                .findByUser1AndUser2OrUser2AndUser1(sender, receiver, sender, receiver)
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setUser1(sender);
                    c.setUser2(receiver);
                    return conversationRepo.save(c);
                });
    }

    public ConversationDTO getOrCreateConversationDTO(Long senderId, Long receiverId) {
        // 1. Tìm hoặc tạo mới conversation
        Conversation conversation = conversationRepo
                .findByUser1AndUser2OrUser2AndUser1(
                        userRepo.findById(senderId).orElseThrow(),
                        userRepo.findById(receiverId).orElseThrow(),
                        userRepo.findById(senderId).orElseThrow(),
                        userRepo.findById(receiverId).orElseThrow()
                )
                .orElseGet(() -> {
                    Conversation newConv = new Conversation();
                    newConv.setUser1(userRepo.findById(senderId).orElseThrow());
                    newConv.setUser2(userRepo.findById(receiverId).orElseThrow());
                    return conversationRepo.save(newConv);
                });

        // 2. Chuyển đổi sang DTO
        ConversationDTO dto = new ConversationDTO();
        // Map các thông tin cơ bản
        dto.setId(conversation.getId());
        dto.setUser1(mapUserToDTO(conversation.getUser1()));
        dto.setUser2(mapUserToDTO(conversation.getUser2()));
        dto.setCreatedAt(conversation.getCreatedAt());

        // Lấy tin nhắn cuối cùng nếu có
        Message lastMessage = getLastMessage(conversation);
        if (lastMessage != null) {
            dto.setLastMessage(mapMessageToDTO(lastMessage));
        }
        // Đếm số tin nhắn chưa đọc
        long unreadCount = countUnreadMessages(conversation, senderId);
        dto.setUnreadCount(unreadCount);

        return dto;
    }

//    get all conversation of user
    public List<ConversationDTO> getUserConversations(Long userId) {
        List<Conversation> conversations = conversationRepo.findAllByUserId(userId);

        return conversations.stream().map(conversation -> {
            ConversationDTO dto = new ConversationDTO();
            // Map các thuộc tính cơ bản
            dto.setId(conversation.getId());
            if (conversation.getUser1().getId().equals(userId)) {
                dto.setUser1(null);
            } else {
                dto.setUser1(mapUserToDTO(conversation.getUser1()));
            }

            // Gán user2 nếu không phải là user đang đăng nhập
            if (conversation.getUser2().getId().equals(userId)) {
                dto.setUser2(null);
            } else {
                dto.setUser2(mapUserToDTO(conversation.getUser2()));
            }
            dto.setCreatedAt(conversation.getCreatedAt());

            // Lấy tin nhắn cuối cùng
            Message lastMessage = getLastMessage(conversation);
            if (lastMessage != null) {
                dto.setLastMessage(mapMessageToDTO(lastMessage));
            }

            // Đếm số tin nhắn chưa đọc
            long unreadCount = countUnreadMessages(conversation, userId);
            dto.setUnreadCount(unreadCount);

            return dto;
        }).collect(Collectors.toList());
    }




    private UserChatDto mapUserToDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserChatDto(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getAvatarUrl()
        );
    }

    private MessageDTO mapMessageToDTO(Message message) {
        if (message == null) {
            return null;
        }
        return new MessageDTO(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getContent(),
                message.getContentType(),
                message.getIsRead(),
                message.getSentAt()
        );
    }
    private long countUnreadMessages(Conversation conversation, Long userId) {
        return messageRepo.countByConversationAndIsReadFalseAndSenderIdNot(conversation, userId);
    }
    private Message getLastMessage(Conversation conversation) {
        // Cách 1: Sử dụng query method trong repository
        return  messageRepo.findTopByConversationOrderBySentAtDesc(conversation)
                .orElse(null);

        // Hoặc cách 2: Lấy từ danh sách messages đã load (nếu đã fetch eager)
    /*
    if (conversation.getMessages() == null || conversation.getMessages().isEmpty()) {
        return null;
    }
    return conversation.getMessages().stream()
            .max(Comparator.comparing(Message::getSentAt))
            .orElse(null);
    */
    }



}


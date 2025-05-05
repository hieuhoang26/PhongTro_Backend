package vn.hhh.phong_tro.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.chat.ChatMessage;
import vn.hhh.phong_tro.dto.chat.UserChatDto;
import vn.hhh.phong_tro.model.Message;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.repository.MessageRepository;
import vn.hhh.phong_tro.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<UserChatDto> getContacts(Long userId) {
        // Get all users except current user
        List<User> users = userRepository.findUsersByIdNot(userId);

        return users.stream().map(user -> {
            UserChatDto dto = new UserChatDto();
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setPhone(user.getPhone());
//            dto.setColor(user.getColor());

            // Get last message
//            Message lastMessage = messageRepository.findLastMessageBetweenUsers(userId, user.getId());
//            if (lastMessage != null) {
//                dto.setLastMessage(lastMessage.getContent());
//            }
            dto.setLastMessage("demo");

            // Get unread count
//            Long unreadCount = messageRepository.countByReceiverIdAndSenderIdAndIsReadFalse(userId, user.getId());
//            dto.setUnreadCount(unreadCount.intValue());
            dto.setUnreadCount(3);
            return dto;
        }).collect(Collectors.toList());
    }

    public Message saveMessage(ChatMessage dto) {
        Message msg = new Message();
        msg.setSenderId(dto.getSenderId());
        msg.setReceiverId(dto.getReceiverId());
        msg.setContent(dto.getContent());
        msg.setContentType(dto.getContentType());
        return messageRepository.save(msg);
    }
//    public List<ChatMessage> getMessagesForUser(Long userId) {
//        List<Message>  list = messageRepository.findByReceiverId(userId);
//        return getChatMessageDtos(list);
//    }
//    public List<ChatMessage> getConversation(Long userAId, Long userBId) {
//        List<Message>  list =  messageRepository.findConversationBetween(userAId, userBId);
//        return getChatMessageDtos(list);
//    }
    public List<ChatMessage> getConversation(String userAId, String userBId) {
        List<Message>  list =  messageRepository.findConversationBetween(userAId, userBId);
        return getChatMessageDtos(list);
    }

    @NotNull
    private List<ChatMessage> getChatMessageDtos(List<Message> list) {
        return list.stream()
                .map(message -> {
                    ChatMessage dto = new ChatMessage();
                    dto.setSenderId(message.getSenderId());
                    dto.setReceiverId(message.getReceiverId());
                    dto.setContent(message.getContent());
                    dto.setContentType(message.getContentType());
                    return dto;
                })
                .toList();
    }
}

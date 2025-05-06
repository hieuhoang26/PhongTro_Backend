package vn.hhh.phong_tro.dto.chat;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDTO {
    private Long id;
    private UserChatDto user1;
    private UserChatDto user2;
    private Timestamp createdAt;
    private MessageDTO lastMessage;
    private long unreadCount;

    // Constructors, getters, setters
}

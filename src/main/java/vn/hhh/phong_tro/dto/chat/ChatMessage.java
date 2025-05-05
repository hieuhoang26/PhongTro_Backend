package vn.hhh.phong_tro.dto.chat;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String content;
    private String contentType; // text, image, etc.
}

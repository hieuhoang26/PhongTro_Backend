package vn.hhh.phong_tro.dto.chat;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private String contentType; // text, image, etc.
}

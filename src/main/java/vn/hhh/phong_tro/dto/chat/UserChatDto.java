package vn.hhh.phong_tro.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChatDto {
    private Long id;
    private String name;
    private String phone;
    private String avatarUrl;
}

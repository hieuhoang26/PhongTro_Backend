package vn.hhh.phong_tro.dto.request;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String type;
    private String title;
    private String content;
}

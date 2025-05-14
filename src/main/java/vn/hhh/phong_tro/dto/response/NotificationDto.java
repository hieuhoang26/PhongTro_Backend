package vn.hhh.phong_tro.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long userId;
    private String type; // e.g. "message", "system"
    private String title;
    private String content;
    private Boolean isRead = false;
    private Timestamp createdAt;
}

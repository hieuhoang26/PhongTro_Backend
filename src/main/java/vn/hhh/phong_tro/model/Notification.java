package vn.hhh.phong_tro.model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
@Entity
@Table(name = "notifications")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type")
    private String type; // e.g. "message", "system"

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT", name = "content")
    private String content;

//    @Column(name = "is_read")
//    private String targetUrl;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

}

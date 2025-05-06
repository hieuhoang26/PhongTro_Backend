package vn.hhh.phong_tro.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@Table(name = "messages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    //    @Column(name = "sender_id")
//    private String senderId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

//    @Column(name = "receiver_id")
//    private String receiverId;

    @Column(columnDefinition = "TEXT", name = "content")
    private String content;

    @Column(name = "content_type")
    private String contentType; // text, image, etc.

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "sent_at")
    private Timestamp sentAt = new Timestamp(System.currentTimeMillis());
}


package vn.hhh.phong_tro.model;
import jakarta.persistence.*;
import lombok.*;
import vn.hhh.phong_tro.util.PostStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String description;
    private BigDecimal price;
    private Float area;
    private String address;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PostType type;

    private Boolean isVip = false;
    private LocalDateTime vipExpiryDate;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.PENDING;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}


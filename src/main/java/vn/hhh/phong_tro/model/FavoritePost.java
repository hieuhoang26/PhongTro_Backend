package vn.hhh.phong_tro.model;

import jakarta.persistence.*;
import lombok.*;
import vn.hhh.phong_tro.model.key.FavoritePostId;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_post")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoritePost {

    @EmbeddedId
    private FavoritePostId id = new FavoritePostId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}


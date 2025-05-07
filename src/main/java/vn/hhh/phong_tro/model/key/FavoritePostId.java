package vn.hhh.phong_tro.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoritePostId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    public FavoritePostId() {}

    public FavoritePostId(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    // Getters and setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    // equals() và hashCode() là bắt buộc khi dùng @Embeddable
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoritePostId)) return false;
        FavoritePostId that = (FavoritePostId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }
}


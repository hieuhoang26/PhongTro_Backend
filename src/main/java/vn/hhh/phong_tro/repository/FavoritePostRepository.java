package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hhh.phong_tro.model.FavoritePost;
import vn.hhh.phong_tro.model.key.FavoritePostId;

import java.util.List;

public interface FavoritePostRepository extends JpaRepository<FavoritePost, FavoritePostId> {

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    List<FavoritePost> findAllByUserId(Long userId);
}


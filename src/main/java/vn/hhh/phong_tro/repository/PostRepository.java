package vn.hhh.phong_tro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.util.PostStatus;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);
}

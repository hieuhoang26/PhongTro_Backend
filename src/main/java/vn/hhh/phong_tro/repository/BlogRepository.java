package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.Blog;
import vn.hhh.phong_tro.util.BlogStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findBySlug(String slug);
    List<Blog> findAllByStatusOrderByCreatedAtDesc(BlogStatus status);
}

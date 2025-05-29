package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hhh.phong_tro.model.Report;

import java.util.List;

public interface ReportPostRepository extends JpaRepository<Report, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);

    List<Report> findAllByPostId(Long postId);

}

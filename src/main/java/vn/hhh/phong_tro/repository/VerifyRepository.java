package vn.hhh.phong_tro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.hhh.phong_tro.model.Verify;
import vn.hhh.phong_tro.util.VerifyStatus;

import java.util.List;

public interface VerifyRepository extends JpaRepository<Verify,Long> {
    boolean existsByUserIdAndStatus(Long userId, VerifyStatus status);
    boolean existsByUserId(Long userId);

    Page<Verify> findByStatus(VerifyStatus status, Pageable pageable);
}

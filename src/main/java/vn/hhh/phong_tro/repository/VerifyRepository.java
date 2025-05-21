package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hhh.phong_tro.model.Verify;
import vn.hhh.phong_tro.util.VerifyStatus;

public interface VerifyRepository extends JpaRepository<Verify,Long> {
    boolean existsByUserIdAndStatus(Long userId, VerifyStatus status);
}

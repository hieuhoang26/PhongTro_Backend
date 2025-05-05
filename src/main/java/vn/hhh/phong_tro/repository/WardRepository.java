package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.Ward;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByDistrictId(Long districtId);
}
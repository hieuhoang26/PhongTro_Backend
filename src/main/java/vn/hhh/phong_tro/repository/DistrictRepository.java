package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hhh.phong_tro.model.District;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByCityId(Long cityId);
}

package vn.hhh.phong_tro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.PostImage;

@Repository
public interface ImageRepository extends JpaRepository<PostImage,Long> {
}

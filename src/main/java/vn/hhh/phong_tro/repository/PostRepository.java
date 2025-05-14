package vn.hhh.phong_tro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.util.PostStatus;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);

    @Query(value = """
                SELECT p.* FROM posts p
                JOIN posts_addresses a ON p.id = a.post_id
                WHERE LEFT(a.geo_hash, 5) IN :geoHashes
                AND p.type_id = :typeId
                AND p.status = 'APPROVED'
            """, nativeQuery = true)
    List<Post> findNearbyPostsByType(
            @Param("geoHashes") Collection<String> geoHashes,
            @Param("typeId") Long typeId
    );


}

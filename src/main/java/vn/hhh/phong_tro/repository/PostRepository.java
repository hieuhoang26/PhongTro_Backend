package vn.hhh.phong_tro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.dto.response.statistic.PostStatistic;
import vn.hhh.phong_tro.dto.response.statistic.StatusStatistic;
import vn.hhh.phong_tro.dto.response.statistic.TypeStatistic;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.util.PostStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findByUserIdAndStatus(Long userId, PostStatus status, Pageable pageable);

    List<Post> findByStatusAndVipExpiryDateBefore(PostStatus status, LocalDateTime now);

    Long countByUserId(Long userId);
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

    // Lấy n post mới nhất theo created_at giảm dần
    @Query("SELECT p FROM Post p WHERE p.status = 'APPROVED' ORDER BY p.createdAt DESC")
    List<Post> findLatestPosts(Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "JOIN p.postAddress pa " +
            "JOIN pa.ward w " +
            "JOIN w.district d " +
            "WHERE p.type.id = :typeId " +
            "AND d.id = :districtId " +
            "AND p.id <> :currentPostId")
    List<Post> findByTypeAndDistrictExcludeCurrent(@Param("typeId") Long typeId,
                                                   @Param("districtId") Long districtId,
                                                   @Param("currentPostId") Long currentPostId);



    // Statistic

    @Query("SELECT COUNT(p) FROM Post p")
    long countPosts();


    @Query(value = "SELECT DATE(created_at) as date, COUNT(*) as postCount " +
            "FROM posts " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY DATE(created_at) ASC", nativeQuery = true)
    List<Object[]> countPostsByDateNative();


    // Thống kê theo trạng thái
    @Query("SELECT new vn.hhh.phong_tro.dto.response.statistic.StatusStatistic(p.status, COUNT(p)) " +
            "FROM Post p GROUP BY p.status")
    List<StatusStatistic> countPostByStatus();

    // Thống kê theo loại phòng
//    @Query("SELECT new vn.hhh.phong_tro.dto.response.statistic.TypeStatistic(pt.name, COUNT(p)) " +
//            "FROM Post p JOIN p.type pt GROUP BY pt.name")
    @Query("SELECT new vn.hhh.phong_tro.dto.response.statistic.TypeStatistic(pt.name, COUNT(p)) " +
            "FROM PostType pt LEFT JOIN pt.posts p " +
            "GROUP BY pt.name")
    List<TypeStatistic> countPostByType();

}

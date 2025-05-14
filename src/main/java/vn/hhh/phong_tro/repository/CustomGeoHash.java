package vn.hhh.phong_tro.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import vn.hhh.phong_tro.model.Post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CustomGeoHash   {
    @PersistenceContext
    private EntityManager entityManager;
    public List<Post> findNearbyPostsByGeoHashPrefixes(Collection<String> geoHashPrefixes, Long typeId) {
        if (geoHashPrefixes == null || geoHashPrefixes.isEmpty()) {
            return new ArrayList<>(); // Tránh lỗi nếu không có geoHash nào
        }

        StringBuilder sqlBuilder = new StringBuilder("""
            SELECT p.* FROM posts p
            JOIN posts_addresses a ON p.id = a.post_id
            WHERE (
        """);

        List<String> likeConditions = new ArrayList<>();
        int i = 0;
        for (String prefix : geoHashPrefixes) {
            likeConditions.add("a.geo_hash LIKE :prefix" + i);
            i++;
        }
        sqlBuilder.append(String.join(" OR ", likeConditions));
        sqlBuilder.append("""
            )
            AND p.type_id = :typeId
            AND p.status = 'APPROVED'
        """);

        Query query = entityManager.createNativeQuery(sqlBuilder.toString(), Post.class);

        // Set parameters
        i = 0;
        for (String prefix : geoHashPrefixes) {
            query.setParameter("prefix" + i, prefix + "%");
            i++;
        }
        query.setParameter("typeId", typeId);

        return query.getResultList();
    }
}

package vn.hhh.phong_tro.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import vn.hhh.phong_tro.dto.request.post.PostFilterRequest;
import vn.hhh.phong_tro.model.*;

public class PostSpecificationsBuilder {

    private final PostFilterRequest filter;

    public PostSpecificationsBuilder(PostFilterRequest filter) {
        this.filter = filter;
    }

    public Specification<Post> build() {
        return Specification.where(hasType())
                .and(priceBetween())
                .and(areaBetween())
                .and(hasCity())
                .and(hasDistrict())
                .and(hasWard())
                .and(hasCategories())
                .and(hasStatus())
                .and(hasIsVip());
    }

    private Specification<Post> hasType() {
        return (root, query, cb) -> {
            if (filter.getTypeId() == null) return null;
            return cb.equal(root.get("type").get("id"), filter.getTypeId());
        };
    }

    private Specification<Post> priceBetween() {
        return (root, query, cb) -> {
            var min = filter.getMinPrice();
            var max = filter.getMaxPrice();
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("price"), min, max);
            return min != null ? cb.greaterThanOrEqualTo(root.get("price"), min) : cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    private Specification<Post> areaBetween() {
        return (root, query, cb) -> {
            var min = filter.getMinArea();
            var max = filter.getMaxArea();
            if (min == null && max == null) return null;
            if (min != null && max != null) return cb.between(root.get("area"), min, max);
            return min != null ? cb.greaterThanOrEqualTo(root.get("area"), min) : cb.lessThanOrEqualTo(root.get("area"), max);
        };
    }

    private Specification<Post> hasCity() {
        return (root, query, cb) -> {
            if (filter.getCityId() == null) return null;
            Join<Post, PostAddress> address = root.join("postAddress", JoinType.INNER);
            Join<PostAddress, Ward> ward = address.join("ward", JoinType.INNER);
            Join<Ward, District> district = ward.join("district", JoinType.INNER);
            Join<District, City> city = district.join("city", JoinType.INNER);
            return cb.equal(city.get("id"), filter.getCityId());
        };
    }

    private Specification<Post> hasDistrict() {
        return (root, query, cb) -> {
            if (filter.getDistrictId() == null) return null;
            Join<Post, PostAddress> address = root.join("postAddress", JoinType.INNER);
            Join<PostAddress, Ward> ward = address.join("ward", JoinType.INNER);
            Join<Ward, District> district = ward.join("district", JoinType.INNER);
            return cb.equal(district.get("id"), filter.getDistrictId());
        };
    }

    private Specification<Post> hasWard() {
        return (root, query, cb) -> {
            if (filter.getWardId() == null) return null;
            Join<Post, PostAddress> address = root.join("postAddress", JoinType.INNER);
            Join<PostAddress, Ward> ward = address.join("ward", JoinType.INNER);
            return cb.equal(ward.get("id"), filter.getWardId());
        };
    }

    private Specification<Post> hasCategories() {
        return (root, query, cb) -> {
            if (filter.getCategoryIds() == null || filter.getCategoryIds().isEmpty()) return null;
            query.distinct(true);
            Join<Post, Category> categories = root.join("categories", JoinType.INNER);
            return categories.get("id").in(filter.getCategoryIds());
        };
    }
    private Specification<Post> hasIsVip() {
        return (root, query, cb) -> {
            if (filter.getIsVip() == null) return null;
            return cb.equal(root.get("isVip"), filter.getIsVip());
        };
    }
    private Specification<Post> hasStatus() {
//        return (root, query, cb) -> {
//            if (filter.getStatus() == null) return null;
//            return cb.equal(root.get("status"), filter.getStatus());
//        };
        return (root, query, cb) -> {
            if (filter.getStatus() == null || filter.getStatus().isEmpty()) return null;
            return root.get("status").in(filter.getStatus());
        };
    }


}







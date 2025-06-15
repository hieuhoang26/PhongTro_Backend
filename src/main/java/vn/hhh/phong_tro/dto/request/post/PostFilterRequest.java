package vn.hhh.phong_tro.dto.request.post;

import lombok.Data;
import vn.hhh.phong_tro.util.PostStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PostFilterRequest {
    private Long typeId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Float minArea;
    private Float maxArea;
    private Long cityId;
    private Long districtId;
    private Long wardId;
    private List<Long> categoryIds;
//    private PostStatus status;
    private List<PostStatus> status;
    private Integer isVip; // lọc bài VIP
    private String sortBy; // ví dụ: "createdAt", "price"
    private String sortDirection; // "asc" hoặc "desc"
}

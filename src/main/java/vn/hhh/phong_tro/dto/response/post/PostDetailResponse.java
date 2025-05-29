package vn.hhh.phong_tro.dto.response.post;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hhh.phong_tro.util.PostStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Float area;
    private String address;
    private Integer type;
    private Integer isVip;
    private LocalDateTime vipExpiryDate;
    private PostStatus status;
    private String username;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> categories;
    private   List<String> images;
    private  Integer wardId;
    private Integer districtId;
    private Integer cityId;
    private Boolean isLike;


    private String nameContact;
    private String phoneContact;
}

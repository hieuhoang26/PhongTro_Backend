package vn.hhh.phong_tro.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hhh.phong_tro.util.PostStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostList {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Float area;
    private List<String> images;
    private String address;
    private Integer isVip;
    private PostStatus status;
    private LocalDateTime vipExpiryDate;
    private String username;
    private String phone;
    private LocalDateTime createdAt;
}


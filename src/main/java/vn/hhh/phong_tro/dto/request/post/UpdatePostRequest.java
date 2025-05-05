package vn.hhh.phong_tro.dto.request.post;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import vn.hhh.phong_tro.util.PostStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdatePostRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Float area;
    private String fullAddress;
    private Long typeId;
    private Boolean isVip;
    private LocalDateTime vipExpiryDate;
    private PostStatus status;
    private MultipartFile[] newImages;
    private List<String> imageUrlsToKeep;
    private MultipartFile video;
    private String videoLink;
    private Long wardId;
    private String detailAddress;
    private List<Long> categoryIds;
}

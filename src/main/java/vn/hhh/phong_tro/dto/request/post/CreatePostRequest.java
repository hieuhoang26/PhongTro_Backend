package vn.hhh.phong_tro.dto.request.post;

import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import vn.hhh.phong_tro.util.PostStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class  CreatePostRequest {
    private Integer userId;
    private String title;
    private String description;
    private BigDecimal price;
    private Float area;
    private String fullAddress;
    private Long typeId;
    private Integer isVip;
    private LocalDateTime vipExpiryDate;
    private PostStatus status;
    private MultipartFile[] images;
    private MultipartFile video;
    private String  videoLink;
    private Integer wardId;
    private String detailAddress;
    private List<String> categories;


    private String nameContact;
    private String phoneContact;
    private Double latitude;
    private Double longitude;

}

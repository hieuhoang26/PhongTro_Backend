package vn.hhh.phong_tro.dto.response.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDetailDto {
    private Long userId;
    private String userName;
    private String reason;
    private LocalDateTime createdAt;
}

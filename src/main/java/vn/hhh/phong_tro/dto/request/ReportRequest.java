package vn.hhh.phong_tro.dto.request;

import lombok.Data;

@Data
public class ReportRequest {
    private Long postId;
    private Long userId;
    private String reason;
}

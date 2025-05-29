package vn.hhh.phong_tro.dto.response.post;

import lombok.Data;

import java.util.List;

@Data
public class ReportedPostDto {
    private Long postId;
    private String title;
    private String status;
    private long reportCount;
    private List<ReportDetailDto> reports;
}

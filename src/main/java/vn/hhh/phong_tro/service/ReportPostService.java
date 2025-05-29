package vn.hhh.phong_tro.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.response.post.ReportDetailDto;
import vn.hhh.phong_tro.dto.response.post.ReportedPostDto;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.model.Report;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.repository.PostRepository;
import vn.hhh.phong_tro.repository.ReportPostRepository;
import vn.hhh.phong_tro.util.PostStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportPostService {
    private static final int MAX_REPORTS = 20;

    private final ReportPostRepository reportPostRepository;

    private final PostRepository postRepository;
    private final UserService userService;

    public void reportPost(Long postId, Long userId, String reason) {
        if (reportPostRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalArgumentException("You have already reported this post.");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        User user = userService.getById(Math.toIntExact(userId));
        // Save the report
        Report report = new Report();
        report.setPost(post);
        report.setUser(user);
        report.setReason(reason);
        reportPostRepository.save(report);

        // Count reports
        long reportCount = reportPostRepository.countByPostId(postId);
        if (reportCount >= MAX_REPORTS && !post.getStatus().equals(PostStatus.REJECTED)) {
            post.setStatus(PostStatus.REJECTED); // auto hide
            postRepository.save(post);
        }
    }

    public List<ReportedPostDto> getReportedPosts() {
        List<Post> posts = postRepository.findAll(); // hoặc chỉ lấy bài bị report
        List<ReportedPostDto> result = new ArrayList<>();

        for (Post post : posts) {
            List<Report> reports = reportPostRepository.findAllByPostId(post.getId());
            if (!reports.isEmpty()) {
                ReportedPostDto dto = new ReportedPostDto();
                dto.setPostId(post.getId());
                dto.setTitle(post.getTitle());
                dto.setStatus(post.getStatus().toString());
                dto.setReportCount(reports.size());

                List<ReportDetailDto> details = reports.stream().map(r -> {
                    ReportDetailDto detail = new ReportDetailDto();
                    detail.setUserId(r.getUser().getId());
                    detail.setUserName(r.getUser().getName());
                    detail.setReason(r.getReason());
                    detail.setCreatedAt(r.getCreatedAt());
                    return detail;
                }).collect(Collectors.toList());

                dto.setReports(details);
                result.add(dto);
            }
        }
        return result;
    }


}

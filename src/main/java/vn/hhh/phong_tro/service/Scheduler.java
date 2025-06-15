package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.repository.PostRepository;
import vn.hhh.phong_tro.util.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scheduler {
    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * *") // Mỗi giờ 1 lần
    public void updateExpiredVipPosts() {
        LocalDateTime now = LocalDateTime.now();

        List<Post> expiredPosts = postRepository.findByStatusAndVipExpiryDateBefore(PostStatus.APPROVED, now);

        for (Post post : expiredPosts) {
            post.setStatus(PostStatus.EXPIRED);
        }

        postRepository.saveAll(expiredPosts);
    }

    @Scheduled(cron = "0 0 2 * * ?") // Chạy mỗi ngày lúc 2:00 AM
    public void deleteExpiredPayingPosts() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        postRepository.deleteOldPayingPosts(PostStatus.PAYING, threeDaysAgo);
        System.out.println("Deleted expired PAYING posts created before " + threeDaysAgo);
    }
}

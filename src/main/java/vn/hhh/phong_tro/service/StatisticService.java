package vn.hhh.phong_tro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import vn.hhh.phong_tro.dto.response.statistic.*;
import vn.hhh.phong_tro.repository.PostRepository;
import vn.hhh.phong_tro.repository.TransactionRepository;
import vn.hhh.phong_tro.repository.UserRepository;
import vn.hhh.phong_tro.util.PostStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TransactionRepository transactionRepository;

    public CardStatistic getCardStats() {
        long users = userRepository.countUsers();
        long posts = postRepository.countPosts();
        long topUpCount = transactionRepository.countTopUpTransactions();
        BigDecimal vipRevenue = transactionRepository.sumTotalPaymentAmount();
        return new CardStatistic(users, posts, topUpCount, vipRevenue);
    }

    public List<PostStatistic> getPostStatisticsByDate() {
        List<Object[]> results = postRepository.countPostsByDateNative();

        List<PostStatistic> statistics = new ArrayList<>();
        for (Object[] row : results) {
            // row[0] = DATE(created_at) => java.sql.Date
            // row[1] = COUNT(*) => BigInteger or Long depending DB
            java.sql.Date sqlDate = (java.sql.Date) row[0];
            LocalDate date = sqlDate.toLocalDate();

            Number countNumber = (Number) row[1];
            Long count = countNumber.longValue();

            statistics.add(new PostStatistic(date, count));
        }
        return statistics;
    }

    public List<RevenueStatistic> getRevenueStatisticsByDate() {
        List<Object[]> results = transactionRepository.calculateRevenueByDateNative();
        List<RevenueStatistic> statistics = new ArrayList<>();

        for (Object[] row : results) {
            String date = (String) row[0];
            BigDecimal totalRevenue = (BigDecimal) row[1];

            statistics.add(new RevenueStatistic(date, totalRevenue));
        }

        return statistics;
    }

    public List<StatusStatistic> getPostCountByStatus() {
//        return postRepository.countPostByStatus();
        List<StatusStatistic> results = postRepository.countPostByStatus();
        Map<PostStatus, Long> map = results.stream()
                .collect(Collectors.toMap(StatusStatistic::getLabel, StatusStatistic::getCount));

        List<StatusStatistic> complete = new ArrayList<>();
        for (PostStatus status : PostStatus.values()) {
            complete.add(new StatusStatistic(status, map.getOrDefault(status, 0L)));
        }

        return complete;
    }

    public List<TypeStatistic> getPostCountByType() {
        return postRepository.countPostByType();
    }
}

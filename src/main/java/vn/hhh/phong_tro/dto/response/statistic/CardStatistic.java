package vn.hhh.phong_tro.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardStatistic {
    private long totalUsers;
    private long totalPosts;
    private long totalTopUpTransactions;
    private BigDecimal totalVipRevenue;
}

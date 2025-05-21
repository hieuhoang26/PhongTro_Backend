package vn.hhh.phong_tro.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
public class RevenueStatistic {
    private String date;
    private BigDecimal totalRevenue;
}

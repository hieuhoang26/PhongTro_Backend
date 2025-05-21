package vn.hhh.phong_tro.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TypeStatistic {
    private String label;
    private Long count;
}

package vn.hhh.phong_tro.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostStatistic {
    private LocalDate  date;
    private Long postCount;
}

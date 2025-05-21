package vn.hhh.phong_tro.dto.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hhh.phong_tro.util.PostStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusStatistic {
    private PostStatus label;
    private Long count;
}

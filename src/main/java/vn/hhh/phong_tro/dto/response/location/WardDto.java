package vn.hhh.phong_tro.dto.response.location;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WardDto {
    private Long id;
    private String name;
}

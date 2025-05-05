package vn.hhh.phong_tro.dto.response.location;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DistrictDto {
    private Long id;
    private String name;
}

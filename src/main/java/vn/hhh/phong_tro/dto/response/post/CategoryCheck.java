package vn.hhh.phong_tro.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCheck {
    private Long id;
    private String name;
    private boolean checked;
}

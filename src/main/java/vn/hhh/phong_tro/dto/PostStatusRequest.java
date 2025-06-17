package vn.hhh.phong_tro.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.hhh.phong_tro.util.PostStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStatusRequest {
    private PostStatus status;
    @Nullable
    private String reason;
}

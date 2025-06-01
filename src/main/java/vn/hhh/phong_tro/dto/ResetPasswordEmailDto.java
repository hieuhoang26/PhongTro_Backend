package vn.hhh.phong_tro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordEmailDto {
    private String phone;
    private String resetLink;
}

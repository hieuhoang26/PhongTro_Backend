package vn.hhh.phong_tro.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangePassRequest {
    @NotBlank(message = "maNv must be not blank")
    private String maNv;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotBlank(message = "newassword must be not blank")
    private String newPassword;

    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;
}

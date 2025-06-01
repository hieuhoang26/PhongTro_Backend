package vn.hhh.phong_tro.dto.request.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "firstName must be not blank")
    private String name;
    @Email(message = "email invalid format")
    private String email;

    @NotNull(message = "password must be not null")
    private String password;

    //@Pattern(regexp = "^\\d{10}$", message = "phone invalid format")
//    @PhoneNumber(message = "phone invalid format")
    private String phone;
    private String role;

    private String avatarUrl;

}

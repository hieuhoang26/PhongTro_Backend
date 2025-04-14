package vn.hhh.phong_tro.dto.request.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SignUpRequest {

    @Length(max = 11)
    @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must be 10 or 11 digits")
    private String phone;

    @Email
    @Length(max = 50)
    private String email;
    @NotBlank
    @Length(max = 1000, min = 4)
    String password;

    @Length(max = 50)
    private String name;

    private Integer role;
}

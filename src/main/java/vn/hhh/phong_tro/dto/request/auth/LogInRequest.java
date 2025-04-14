package vn.hhh.phong_tro.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogInRequest {
    @Length(max = 11)
    @Pattern(regexp = "^\\d{10,11}$", message = "Phone number must be 10 or 11 digits")
    @NotBlank
    String phone;

    @NotBlank
    String password;
}

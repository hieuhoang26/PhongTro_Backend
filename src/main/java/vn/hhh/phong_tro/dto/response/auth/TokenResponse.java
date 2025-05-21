package vn.hhh.phong_tro.dto.response.auth;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse implements Serializable {
    private String id;
//    private List<String> roles;
    private String role;
    private String phone;
    private Boolean verify;
    private String accessToken;
    private String refreshToken;
    private String message;

}

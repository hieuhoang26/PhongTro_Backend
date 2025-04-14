package vn.hhh.phong_tro.dto.response.user;

import jakarta.persistence.*;
import lombok.*;
import vn.hhh.phong_tro.model.Role;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String avatarUrl;
    private String role;
    private Boolean isActive ;
    private Date createdAt;
}

package vn.hhh.phong_tro.dto.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.util.VerifyStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequest {

    private Integer userId;
    private String cccdNumber;
    private MultipartFile frontImageUrl;
    private String extractedName;
    private LocalDateTime extractedDob;
    private String extractedAddress;
}

package vn.hhh.phong_tro.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayByWalletRequest {
    private Integer userId;
    private Integer postId;
    private BigDecimal amount;
    private Integer isVip;
    private LocalDateTime dateTime;
}

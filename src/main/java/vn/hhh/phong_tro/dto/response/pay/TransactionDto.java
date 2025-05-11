package vn.hhh.phong_tro.dto.response.pay;

import jakarta.persistence.*;
import lombok.*;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.util.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private Long user;
    private TransactionType type; // Enum: TOP_UP, PAYMENT
    private BigDecimal amount;
    private String method;
    private String description;
    private LocalDateTime createdAt;
}

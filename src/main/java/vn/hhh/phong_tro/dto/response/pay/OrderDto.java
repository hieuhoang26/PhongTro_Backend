package vn.hhh.phong_tro.dto.response.pay;

import jakarta.persistence.*;
import lombok.*;
import vn.hhh.phong_tro.model.Post;
import vn.hhh.phong_tro.model.User;
import vn.hhh.phong_tro.util.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private Long user;
    private Long post;
    private String paymentMethod; // e.g. WALLET, VNPAY

    private BigDecimal amount;

    private OrderStatus status = OrderStatus.PENDING;
    private LocalDateTime createdAt ;

}

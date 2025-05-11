package vn.hhh.phong_tro.model;

import jakarta.persistence.*;
import lombok.*;
import vn.hhh.phong_tro.util.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // Enum: TOP_UP, PAYMENT

    @Column(nullable = false)
    private BigDecimal amount;

    private String method;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
}

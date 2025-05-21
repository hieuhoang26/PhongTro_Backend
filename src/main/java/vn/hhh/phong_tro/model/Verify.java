package vn.hhh.phong_tro.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import vn.hhh.phong_tro.util.VerifyStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "verify")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Verify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "cccd_number", nullable = false)
    private String cccdNumber;

    @Column(name = "front_image_url")
    private String frontImageUrl;

    @Column(name = "extracted_name")
    private String extractedName;

    @Column(name = "extracted_dob")
//    @Temporal(TemporalType.DATE)
    private LocalDateTime extractedDob;

    @Column(name = "extracted_address")
    private String extractedAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VerifyStatus status = VerifyStatus.PENDING;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;


    @PrePersist
    protected void onCreate() {
        createdAt  = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        approvedAt = LocalDateTime.now();
    }
}


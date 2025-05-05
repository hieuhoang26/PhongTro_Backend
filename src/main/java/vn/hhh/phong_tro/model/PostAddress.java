package vn.hhh.phong_tro.model;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts_addresses")
public class PostAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    private Double latitude;
    private Double longitude;
}

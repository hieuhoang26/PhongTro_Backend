package vn.hhh.phong_tro.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "post_types")
public class PostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    private List<Post> posts;
}

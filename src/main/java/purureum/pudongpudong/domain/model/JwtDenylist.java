package purureum.pudongpudong.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtDenylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String signature;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public JwtDenylist(String signature, LocalDateTime expiresAt) {
        this.signature = signature;
        this.expiresAt = expiresAt;
    }
}

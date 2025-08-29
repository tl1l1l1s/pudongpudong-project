package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.JwtDenylist;

import java.time.LocalDateTime;

@Repository
public interface JwtDenylistRepository extends JpaRepository<JwtDenylist, Long> {
    boolean existsBySignature(String signature);
    void deleteByExpiresAtBefore(LocalDateTime now);
}

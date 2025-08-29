package purureum.pudongpudong.infrastructure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.repository.JwtDenylistRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DenylistCleanupTask {

    private final JwtDenylistRepository jwtDenylistRepository;

    // 매일 새벽 4시에 실행
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void cleanupExpiredDenylistedTokens() {
        log.info("만료된 무효 토큰 정리 작업 시작...");
        LocalDateTime now = LocalDateTime.now();
        jwtDenylistRepository.deleteByExpiresAtBefore(now);
        log.info("만료된 무효 토큰 정리 작업 완료.");
    }
}

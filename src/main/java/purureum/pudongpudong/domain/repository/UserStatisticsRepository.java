package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.UserStatistics;

import java.util.Optional;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
	Optional<UserStatistics> findByUserId(Long userId);
}
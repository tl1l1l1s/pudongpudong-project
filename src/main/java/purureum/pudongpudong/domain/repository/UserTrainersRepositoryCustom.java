package purureum.pudongpudong.domain.repository;

import purureum.pudongpudong.domain.model.UserTrainers;

import java.util.List;
import java.util.Optional;

public interface UserTrainersRepositoryCustom {
	List<UserTrainers> findByUserId(Long userId);
	Integer countByUserId(Long userId);
	Optional<UserTrainers> findByUserIdAndTrainerId(Long userId, Long trainerId);
}
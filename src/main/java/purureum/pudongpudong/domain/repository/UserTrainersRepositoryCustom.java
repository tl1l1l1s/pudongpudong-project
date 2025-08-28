package purureum.pudongpudong.domain.repository;

import purureum.pudongpudong.domain.model.UserTrainers;

import java.util.List;

public interface UserTrainersRepositoryCustom {
	List<UserTrainers> findByUserId(Long userId);
	Integer countByUserId(Long userId);
}
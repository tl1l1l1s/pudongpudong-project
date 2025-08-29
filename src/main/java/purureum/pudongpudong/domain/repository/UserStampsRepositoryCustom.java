package purureum.pudongpudong.domain.repository;

import purureum.pudongpudong.domain.model.Species;
import purureum.pudongpudong.domain.model.UserStamps;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UserStampsRepositoryCustom {
	List<UserStamps> findBySessionId(Long sessionId);
	List<Species> findSpeciesByParkName(String parkName);
	Integer countDistinctSpeciesByUserId(Long userId);
	Map<String, Integer> countSpeciesByUserIdAndPark(Long userId);
	Integer countTotalSpecies();
	List<Species> findCollectedSpeciesByUserIdAndParkId(Long userId, String parkId);
	List<UserStamps> findByUserAndDateRangeWithDetails(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
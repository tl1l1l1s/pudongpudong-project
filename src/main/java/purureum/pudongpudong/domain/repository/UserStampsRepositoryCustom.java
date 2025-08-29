package purureum.pudongpudong.domain.repository;

import purureum.pudongpudong.domain.model.Species;
import purureum.pudongpudong.domain.model.UserStamps;

import java.util.List;
import java.util.Map;

public interface UserStampsRepositoryCustom {
	List<UserStamps> findBySessionId(Long sessionId);
	List<Species> findSpeciesByParkName(String parkName);
	Integer countDistinctSpeciesByUserId(Long userId);
	Map<String, Integer> countSpeciesByUserIdAndPark(Long userId);
	Integer countTotalSpecies();
	List<Species> findCollectedSpeciesByUserIdAndParkName(Long userId, String parkName);
	List<Species> findCollectedSpeciesByUserIdAndParkId(Long userId, String parkId);
}
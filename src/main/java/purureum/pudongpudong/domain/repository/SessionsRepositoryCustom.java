package purureum.pudongpudong.domain.repository;

import purureum.pudongpudong.domain.model.Sessions;
import purureum.pudongpudong.infrastructure.dto.RunningStatisticsDto;

import java.util.List;

public interface SessionsRepositoryCustom {
	List<Sessions> findByUserIdAndYearMonth(Long userId, int year, int month);
	RunningStatisticsDto findStatisticsByUserIdAndYearMonth(Long userId, int year, int month);
}
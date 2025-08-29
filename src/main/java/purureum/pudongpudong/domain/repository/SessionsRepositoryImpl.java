package purureum.pudongpudong.domain.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import purureum.pudongpudong.domain.model.Sessions;
import purureum.pudongpudong.infrastructure.dto.RunningStatisticsDto;

import java.util.List;

import static purureum.pudongpudong.domain.model.QSessions.sessions;
import static purureum.pudongpudong.domain.model.QParks.parks;

@RequiredArgsConstructor
public class SessionsRepositoryImpl implements SessionsRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<Sessions> findByUserIdAndYearMonth(Long userId, int year, int month) {
		return queryFactory
				.selectFrom(sessions)
				.join(sessions.park, parks).fetchJoin()
				.where(sessions.user.id.eq(userId)
						.and(sessions.createdAt.year().eq(year))
						.and(sessions.createdAt.month().eq(month)))
				.orderBy(sessions.createdAt.desc())
				.fetch();
	}
	
	@Override
	public RunningStatisticsDto findStatisticsByUserIdAndYearMonth(Long userId, int year, int month) {
		return queryFactory
				.select(Projections.constructor(RunningStatisticsDto.class,
						sessions.count().intValue(),
						sessions.distance.sum().coalesce(0.0),
						sessions.caloriesBurned.sum().coalesce(0.0)))
				.from(sessions)
				.where(sessions.user.id.eq(userId)
						.and(sessions.createdAt.year().eq(year))
						.and(sessions.createdAt.month().eq(month)))
				.fetchOne();
	}
}
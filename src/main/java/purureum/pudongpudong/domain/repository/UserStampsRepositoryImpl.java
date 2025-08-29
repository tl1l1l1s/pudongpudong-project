package purureum.pudongpudong.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import purureum.pudongpudong.domain.model.Species;
import purureum.pudongpudong.domain.model.UserStamps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static purureum.pudongpudong.domain.model.QUserStamps.userStamps;
import static purureum.pudongpudong.domain.model.QSpecies.species;
import static purureum.pudongpudong.domain.model.QParkSpecies.parkSpecies;
import static purureum.pudongpudong.domain.model.QSessions.sessions;
import static purureum.pudongpudong.domain.model.QParks.parks;

@RequiredArgsConstructor
public class UserStampsRepositoryImpl implements UserStampsRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<UserStamps> findBySessionId(Long sessionId) {
		return queryFactory
				.selectFrom(userStamps)
				.join(userStamps.species, species).fetchJoin()
				.where(userStamps.session.id.eq(sessionId))
				.fetch();
	}
	
	@Override
	public List<Species> findSpeciesByParkName(String parkName) {
		return queryFactory
				.select(parkSpecies.species)
				.from(parkSpecies)
				.join(parkSpecies.park, parks)
				.where(parks.placeName.eq(parkName))
				.fetch();
	}
	
	@Override
	public Integer countDistinctSpeciesByUserId(Long userId) {
		Long count = queryFactory
				.select(userStamps.species.id.countDistinct())
				.from(userStamps)
				.where(userStamps.user.id.eq(userId))
				.fetchOne();
		return count != null ? count.intValue() : 0;
	}
	
	@Override
	public Map<String, Integer> countSpeciesByUserIdAndPark(Long userId) {
		List<Object[]> results = queryFactory
				.select(parks.placeName, userStamps.species.id.countDistinct())
				.from(userStamps)
				.join(userStamps.session, sessions)
				.join(sessions.park, parks)
				.where(userStamps.user.id.eq(userId))
				.groupBy(parks.placeName)
				.fetch()
				.stream()
				.map(tuple -> new Object[]{tuple.get(parks.placeName), tuple.get(userStamps.species.id.countDistinct())})
				.toList();
		
		return results.stream()
				.collect(Collectors.toMap(
						result -> (String) result[0],
						result -> ((Long) result[1]).intValue()
				));
	}
	
	@Override
	public Integer countTotalSpecies() {
		Long count = queryFactory
				.select(species.id.countDistinct())
				.from(species)
				.fetchOne();
		return count != null ? count.intValue() : 0;
	}
	
	@Override
	public List<Species> findCollectedSpeciesByUserIdAndParkName(Long userId, String parkName) {
		return queryFactory
				.select(species).distinct()
				.from(userStamps)
				.join(userStamps.species, species)
				.join(userStamps.session, sessions)
				.join(sessions.park, parks)
				.where(userStamps.user.id.eq(userId)
						.and(parks.placeName.eq(parkName)))
				.fetch();
	}
	
	@Override
	public List<Species> findCollectedSpeciesByUserIdAndParkId(Long userId, String parkId) {
		return queryFactory
				.select(species).distinct()
				.from(userStamps)
				.join(userStamps.species, species)
				.join(userStamps.session, sessions)
				.where(userStamps.user.id.eq(userId)
						.and(sessions.park.id.eq(parkId)))
				.fetch();
	}
}
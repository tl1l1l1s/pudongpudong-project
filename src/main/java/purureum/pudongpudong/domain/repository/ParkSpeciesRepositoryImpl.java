package purureum.pudongpudong.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static purureum.pudongpudong.domain.model.QParkSpecies.parkSpecies;
import static purureum.pudongpudong.domain.model.QParks.parks;

@RequiredArgsConstructor
public class ParkSpeciesRepositoryImpl implements ParkSpeciesRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public Integer countByParkName(String parkName) {
		Long count = queryFactory
				.select(parkSpecies.count())
				.from(parkSpecies)
				.join(parkSpecies.park, parks)
				.where(parks.placeName.eq(parkName))
				.fetchOne();
		return count != null ? count.intValue() : 0;
	}
}
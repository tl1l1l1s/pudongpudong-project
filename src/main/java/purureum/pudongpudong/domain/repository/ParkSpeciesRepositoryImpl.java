package purureum.pudongpudong.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import purureum.pudongpudong.domain.model.Species;

import java.util.List;

import static purureum.pudongpudong.domain.model.QParkSpecies.parkSpecies;
import static purureum.pudongpudong.domain.model.QParks.parks;
import static purureum.pudongpudong.domain.model.QSpecies.species;

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
	
	@Override
	public List<Species> findSpeciesByParkName(String parkName) {
		return queryFactory
				.select(species)
				.from(parkSpecies)
				.join(parkSpecies.park, parks)
				.join(parkSpecies.species, species)
				.where(parks.placeName.eq(parkName))
				.fetch();
	}
	
	@Override
	public Integer countByParkId(String parkId) {
		Long count = queryFactory
				.select(parkSpecies.count())
				.from(parkSpecies)
				.where(parkSpecies.park.id.eq(parkId))
				.fetchOne();
		return count != null ? count.intValue() : 0;
	}
	
	@Override
	public List<Species> findSpeciesByParkId(String parkId) {
		return queryFactory
				.select(species)
				.from(parkSpecies)
				.join(parkSpecies.species, species)
				.where(parkSpecies.park.id.eq(parkId))
				.fetch();
	}
}
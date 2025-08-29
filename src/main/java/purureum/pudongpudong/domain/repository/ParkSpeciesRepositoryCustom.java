package purureum.pudongpudong.domain.repository;

import purureum.pudongpudong.domain.model.Species;

import java.util.List;

public interface ParkSpeciesRepositoryCustom {
	Integer countByParkName(String parkName);
	List<Species> findSpeciesByParkName(String parkName);
	Integer countByParkId(String parkId);
	List<Species> findSpeciesByParkId(String parkId);
}
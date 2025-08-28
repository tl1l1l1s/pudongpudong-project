package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.ParkSpecies;
import purureum.pudongpudong.domain.model.id.ParkSpeciesId;

@Repository
public interface ParkSpeciesRepository extends JpaRepository<ParkSpecies, ParkSpeciesId>, ParkSpeciesRepositoryCustom {
}
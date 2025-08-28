package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.UserStamps;

import java.util.List;

@Repository
public interface UserStampsRepository extends JpaRepository<UserStamps, Long> {
	
	@Query("SELECT us FROM UserStamps us JOIN FETCH us.species " +
			"WHERE us.session.id = :sessionId")
	List<UserStamps> findBySessionId(@Param("sessionId") Long sessionId);
	
	@Query("SELECT sp.species FROM ParkSpecies sp " +
			"WHERE sp.park.placeName = :parkName " +
			"ORDER BY FUNCTION('RAND')")
	List<purureum.pudongpudong.domain.model.Species> findRandomSpeciesByParkName(@Param("parkName") String parkName);
}
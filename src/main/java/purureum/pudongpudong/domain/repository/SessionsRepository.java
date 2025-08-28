package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.Sessions;

import java.util.List;

@Repository
public interface SessionsRepository extends JpaRepository<Sessions, Long> {
	
	@Query("SELECT s FROM Sessions s JOIN FETCH s.park " +
			"WHERE s.user.id = :userId " +
			"AND YEAR(s.createdAt) = :year " +
			"AND MONTH(s.createdAt) = :month " +
			"ORDER BY s.createdAt DESC")
	List<Sessions> findByUserIdAndYearMonth(@Param("userId") Long userId, 
	                                       @Param("year") int year, 
	                                       @Param("month") int month);
	
	@Query("SELECT COUNT(s), COALESCE(SUM(s.distance), 0), COALESCE(SUM(s.caloriesBurned), 0) " +
			"FROM Sessions s " +
			"WHERE s.user.id = :userId " +
			"AND YEAR(s.createdAt) = :year " +
			"AND MONTH(s.createdAt) = :month")
	Object[] findStatisticsByUserIdAndYearMonth(@Param("userId") Long userId, 
	                                          @Param("year") int year, 
	                                          @Param("month") int month);
}
package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.Trainers;

@Repository
public interface TrainersRepository extends JpaRepository<Trainers, Long> {
	
}
package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.UserTrainers;
import purureum.pudongpudong.domain.model.id.UserTrainersId;

@Repository
public interface UserTrainersRepository extends JpaRepository<UserTrainers, UserTrainersId>, UserTrainersRepositoryCustom {
}
package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.UserStamps;

@Repository
public interface UserStampsRepository extends JpaRepository<UserStamps, Long>, UserStampsRepositoryCustom {
}
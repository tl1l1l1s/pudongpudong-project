package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.Sessions;

@Repository
public interface SessionsRepository extends JpaRepository<Sessions, Long>, SessionsRepositoryCustom {
}
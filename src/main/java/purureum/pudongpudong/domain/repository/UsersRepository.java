package purureum.pudongpudong.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purureum.pudongpudong.domain.model.Users;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByProviderId(String providerId);
	boolean existsByProviderId(String providerId);
}
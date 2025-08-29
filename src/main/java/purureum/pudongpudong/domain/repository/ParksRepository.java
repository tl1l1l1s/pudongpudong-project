package purureum.pudongpudong.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import purureum.pudongpudong.domain.model.Parks;

import java.util.Optional;


public interface ParksRepository extends JpaRepository<Parks, String> {
	Optional<Parks> findByPlaceName(String placeName);
}

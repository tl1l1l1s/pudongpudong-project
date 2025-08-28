package purureum.pudongpudong.domain.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import purureum.pudongpudong.domain.model.Parks;

import java.util.List;


public interface ParksRepository extends JpaRepository<Parks, String> {
}

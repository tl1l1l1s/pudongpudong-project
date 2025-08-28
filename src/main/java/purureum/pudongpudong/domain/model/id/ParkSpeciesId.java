package purureum.pudongpudong.domain.model.id;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParkSpeciesId implements Serializable {
	private String parkId;
	private Long speciesId;
}
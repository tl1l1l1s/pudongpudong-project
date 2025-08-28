package purureum.pudongpudong.domain.model;

import jakarta.persistence.*;
import lombok.*;
import purureum.pudongpudong.domain.model.id.ParkSpeciesId;
import purureum.pudongpudong.global.common.domain.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParkSpecies extends BaseEntity {
	
	@EmbeddedId
	private ParkSpeciesId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("parkId")
	@JoinColumn(name = "park_id")
	private Parks park;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("speciesId")
	@JoinColumn(name = "species_id")
	private Species species;
}
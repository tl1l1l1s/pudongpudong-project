package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectionTrainerDto {
	private String parkName;
	private String trainerName;
	private Integer totalSpecies;
	private Integer collectedSpecies;
}
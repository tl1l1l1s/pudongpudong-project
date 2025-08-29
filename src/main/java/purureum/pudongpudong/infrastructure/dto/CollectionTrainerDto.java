package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectionTrainerDto {
	private String parkName;
	private Long trainerId;
	private String trainerName;
	private Integer collectedSpecies;
	private Integer totalSpecies;
	private Double completionPercentage;
}
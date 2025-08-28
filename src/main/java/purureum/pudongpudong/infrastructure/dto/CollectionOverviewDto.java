package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectionOverviewDto {
	private Integer unlockedTrainers;
	private Integer lockedTrainers;
	private Integer collectedStamps;
	private Double completionPercentage;
}
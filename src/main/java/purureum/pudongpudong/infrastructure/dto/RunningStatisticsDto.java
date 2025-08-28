package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RunningStatisticsDto {
	private Integer running;
	private Double distance;
	private Double calories;
}
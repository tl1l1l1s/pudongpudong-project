package purureum.pudongpudong.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunningStatisticsDto {
	private Integer running;
	private Double distance;
	private Double calories;
}
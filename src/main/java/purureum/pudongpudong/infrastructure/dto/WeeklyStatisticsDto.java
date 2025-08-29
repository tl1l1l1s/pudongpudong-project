package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeeklyStatisticsDto {
    private final Integer running;
    private final Double distance;
    private final Integer collectedStamps;
}

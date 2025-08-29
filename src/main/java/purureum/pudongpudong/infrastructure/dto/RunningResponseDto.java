package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RunningResponseDto {
	private RunningStatisticsDto statistics;
	private List<RunningDataDto> data;
}
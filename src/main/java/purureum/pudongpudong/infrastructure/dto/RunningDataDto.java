package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RunningDataDto {
	private Integer date;
	private Integer duration;
	private Double distance;
	private Double pace;
	private Double calories;
	private String location;
	private String mood;
	private java.util.List<StampDto> stamps;
}
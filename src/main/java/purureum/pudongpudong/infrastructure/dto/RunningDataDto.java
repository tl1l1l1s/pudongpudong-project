package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RunningDataDto {
	private Integer date;
	private Integer duration; // 분
	private Double distance; // km
	private Double pace; // 분/km
	private Double calories;
	private String location;
	private String mood; // 러닝 무드
	private java.util.List<String> stamps; // 스탬프 목록
}
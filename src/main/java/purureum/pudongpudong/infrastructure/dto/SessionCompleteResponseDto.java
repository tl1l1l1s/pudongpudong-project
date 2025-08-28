package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionCompleteResponseDto {
	private Long sessionId;
	private Integer duration; // 분
	private Double distance; // km
	private Double pace; // 분/km
	private Double calories;
	private String parkName;
	private String mood; // 러닝 무드
	private List<String> stamps; // 획득한 스탬프 목록
}
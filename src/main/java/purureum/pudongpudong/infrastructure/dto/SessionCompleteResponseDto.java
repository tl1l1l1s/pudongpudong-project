package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionCompleteResponseDto {
	private Long sessionId;
	private Integer duration;
	private Double distance;
	private Double pace;
	private Double calories;
	private String parkName;
	private String mood;
	private List<String> stamps;
}
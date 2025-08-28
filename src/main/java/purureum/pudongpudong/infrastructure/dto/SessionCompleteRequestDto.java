package purureum.pudongpudong.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import purureum.pudongpudong.domain.model.enums.Mood;

@Getter
@NoArgsConstructor
public class SessionCompleteRequestDto {
	
	@NotNull
	@Positive
	private Integer duration; // 분 단위
	
	@NotNull
	@Positive
	private Double distance; // km 단위
	
	@NotNull
	@Positive
	private Double calories;
	
	@NotBlank
	private String parkName;
	
	@NotNull
	private Mood mood; // 러닝 무드
}
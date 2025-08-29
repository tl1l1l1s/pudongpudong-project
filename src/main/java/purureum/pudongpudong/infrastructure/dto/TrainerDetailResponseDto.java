package purureum.pudongpudong.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDetailResponseDto {
	private Long trainerId;
	private String trainerName;
	private String description;
	private List<TrainerStampDto> stamps;
	private Integer collectedStamps;
	private Integer totalStamps;
}
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
	private String trainerName;
	private String description;
	private Integer level;
	private Integer experience;
	private Integer toNextLevel;
	private String comment;
	private List<TrainerStampDto> stamps;
	private Integer collectedStamps;
	private Integer totalStamps;
}
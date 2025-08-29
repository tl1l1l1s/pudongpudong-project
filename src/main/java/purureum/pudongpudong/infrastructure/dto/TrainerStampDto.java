package purureum.pudongpudong.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerStampDto {
	private String speciesName;
	private String emoji;
	private boolean isCollected;
}
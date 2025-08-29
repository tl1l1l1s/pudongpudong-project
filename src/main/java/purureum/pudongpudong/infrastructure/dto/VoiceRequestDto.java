package purureum.pudongpudong.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoiceRequestDto {
	
	@NotBlank
	private String query;
	
	@NotNull
	private Double latitude;
	
	@NotNull
	private Double longitude;
	
	private Integer remainingTimeMinutes;
	private Integer remainingTimeSeconds;
	private Double currentDistance;
	private Double currentPace;
}
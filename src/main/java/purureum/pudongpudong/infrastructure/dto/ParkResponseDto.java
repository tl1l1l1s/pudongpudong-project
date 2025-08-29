package purureum.pudongpudong.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import purureum.pudongpudong.domain.model.Parks;
import purureum.pudongpudong.infrastructure.dto.api.KakaoNaviApiResponseDto;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParkResponseDto {
	String id;
	String parkName;
	Double longitude;
	Double latitude;
	Integer distance;
	Integer time;
	
	public static ParkResponseDto toDto(Parks park, KakaoNaviApiResponseDto.Route response) {
		return ParkResponseDto.builder()
				.id(park.getId())
				.parkName(park.getPlaceName())
				.longitude(park.getLongitude())
				.latitude(park.getLatitude())
				.distance(response.getSummary().getDistance())
				.time(response.getSummary().getDuration()/60)
				.build();
	}
}

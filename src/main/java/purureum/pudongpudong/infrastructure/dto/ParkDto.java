package purureum.pudongpudong.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import purureum.pudongpudong.domain.model.Parks;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkDto {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("place_name")
	private String parkName;
	
	@JsonProperty("address_name")
	private String addressName;
	
	@JsonProperty("road_address_name")
	private String roadAddressName;
	
	@JsonProperty("x")
	private String longitude;
	
	@JsonProperty("y")
	private String latitude;
	
	public Parks toEntity() {
		return Parks.builder()
				.id(this.getId())
				.placeName(this.getParkName())
				.addressName(this.getAddressName())
				.roadAddressName(this.getRoadAddressName())
				.latitude(Double.parseDouble(this.getLatitude()))
				.longitude(Double.parseDouble(this.getLongitude()))
				.build();
	}
}

package purureum.pudongpudong.domain.model;

import jakarta.persistence.*;
import lombok.*;
import purureum.pudongpudong.global.common.domain.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Parks extends BaseEntity {
	
	@Id
	private String id;
	
	@Column(nullable = false, length = 50)
	private String placeName;
	
	private String addressName;
	
	private String roadAddressName;
	
	@Column(columnDefinition = "DECIMAL(10, 7)", nullable = false)
	private Double longitude;
	
	@Column(columnDefinition = "DECIMAL(10, 7)", nullable = false)
	private Double latitude;
	
	@Builder.Default
	@Column(length = 20)
	private String emoji  = "🌱";
}
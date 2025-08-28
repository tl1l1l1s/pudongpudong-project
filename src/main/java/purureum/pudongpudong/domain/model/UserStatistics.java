package purureum.pudongpudong.domain.model;

import jakarta.persistence.*;
import lombok.*;
import purureum.pudongpudong.global.common.domain.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserStatistics extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Integer runCount;
	
	@Column(nullable = false)
	private Double totalDistance;
	
	@Column(nullable = false)
	private Double totalCaloriesBurned;
	
	@Column(nullable = false)
	private Integer totalTrainersUnlocked;
	
	@Column(nullable = false)
	private Integer totalStampsCollected;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private Users user;
}

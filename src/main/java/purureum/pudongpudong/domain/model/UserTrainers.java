package purureum.pudongpudong.domain.model;

import jakarta.persistence.*;
import lombok.*;
import purureum.pudongpudong.domain.model.id.UserTrainersId;
import purureum.pudongpudong.global.common.domain.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserTrainers extends BaseEntity {
	
	@EmbeddedId
	private UserTrainersId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private Users user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("trainerId")
	@JoinColumn(name = "trainer_id")
	private Trainers trainer;
}

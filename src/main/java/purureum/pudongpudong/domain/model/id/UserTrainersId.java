package purureum.pudongpudong.domain.model.id;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserTrainersId implements Serializable {
	private Long userId;
	private Long trainerId;
}
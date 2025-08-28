package purureum.pudongpudong.domain.model;

import jakarta.persistence.*;
import lombok.*;
import purureum.pudongpudong.domain.model.enums.ProviderType;
import purureum.pudongpudong.global.common.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Users extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(nullable = false, unique = true)
	private String providerId;
	
	private ProviderType provider;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserStatistics userStatistics;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Sessions> sessions = new ArrayList<>();
}

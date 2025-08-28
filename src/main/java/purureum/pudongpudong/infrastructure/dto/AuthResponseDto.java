package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;
import purureum.pudongpudong.domain.model.enums.ProviderType;

@Getter
@Builder
public class AuthResponseDto {
	private Long userId;
	private String name;
	private String providerId;
	private ProviderType provider;
	private String token;
	private boolean isNewUser;
}
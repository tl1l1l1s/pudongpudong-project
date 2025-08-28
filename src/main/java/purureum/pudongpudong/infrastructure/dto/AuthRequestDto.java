package purureum.pudongpudong.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import purureum.pudongpudong.domain.model.enums.ProviderType;

@Getter
@NoArgsConstructor
public class AuthRequestDto {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String providerId;
	
	@NotNull
	private ProviderType provider;
}
package purureum.pudongpudong.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI pudongpudongAPI() {
		Info info = new Info()
				.title("푸동푸동 API")
				.description("2025년 K-HTML 해커톤 참여 프로젝트 푸동푸동의 API 문서입니다.")
				.version("1.0.0");
		
		String bearerTokenSchemeName = "Bearer Authentication";
		SecurityScheme bearerTokenScheme = new SecurityScheme()
				.name(bearerTokenSchemeName)
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT");
		
		Components components = new Components()
				.addSecuritySchemes(bearerTokenSchemeName, bearerTokenScheme);
		
		SecurityRequirement securityRequirement = new SecurityRequirement()
				.addList(bearerTokenSchemeName);
		
		return new OpenAPI()
				.addServersItem(new Server().url("/"))
				.info(info)
				.addSecurityItem(securityRequirement)
				.components(components);
	}
}

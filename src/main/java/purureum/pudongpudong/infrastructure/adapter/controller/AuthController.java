package purureum.pudongpudong.infrastructure.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import purureum.pudongpudong.application.service.AuthService;
import purureum.pudongpudong.global.apiPayload.ApiResponse;
import purureum.pudongpudong.infrastructure.dto.AuthRequestDto;
import purureum.pudongpudong.infrastructure.dto.AuthResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "인증 관련 API입니다.")
public class AuthController {
	
	private final AuthService authService;
	
	@Operation(
			summary = "소셜 로그인/회원가입",
			description = "카카오/네이버 소셜 로그인 및 회원가입을 처리합니다."
	)
	@PostMapping("/signup")
	public ApiResponse<AuthResponseDto> signUp(@Valid @RequestBody AuthRequestDto request) {
		return ApiResponse.onSuccess(authService.signUp(request));
	}

	@Operation(
			summary = "로그아웃",
			description = "사용자의 현재 토큰을 무효화 처리합니다."
	)
	@PostMapping("/logout")
	public ApiResponse<Void> logout(@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
		authService.logout(authorizationHeader);
		return ApiResponse.onSuccess(null);
	}
}
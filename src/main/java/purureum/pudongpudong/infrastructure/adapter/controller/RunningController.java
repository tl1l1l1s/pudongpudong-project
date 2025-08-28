package purureum.pudongpudong.infrastructure.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import purureum.pudongpudong.application.service.RunningService;
import purureum.pudongpudong.global.apiPayload.ApiResponse;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.handler.ApiHandler;
import purureum.pudongpudong.global.util.JwtUtil;
import purureum.pudongpudong.infrastructure.dto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/running")
@Tag(name = "Running API", description = "러닝 기록 관련 API입니다.")
public class RunningController {
	
	private final RunningService runningService;
	private final JwtUtil jwtUtil;
	
	@Operation(
			summary = "[캘린더 탭] 러닝 기록 조회",
			description = "지정된 연도와 월의 사용자 러닝 기록을 조회합니다."
	)
	@GetMapping
	public ApiResponse<RunningResponseDto> getRunningCalendar(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam int year,
			@RequestParam int month) {
		
		// JWT 토큰에서 사용자 ID 추출
		String token = extractTokenFromHeader(authorizationHeader);
		Long userId = jwtUtil.getUserIdFromToken(token);
		
		if (userId == null) {
			throw new ApiHandler(ErrorStatus._UNAUTHORIZED);
		}
		
		log.info("러닝 캘린더 조회 요청: userId={}, year={}, month={}", userId, year, month);
		
		RunningResponseDto response = runningService.getRunningCalendar(userId, year, month);
		return ApiResponse.onSuccess(response);
	}
	
	@Operation(
			summary = "[] 러닝 완료",
			description = "러닝 세션을 완료하고 결과를 저장합니다."
	)
	@PostMapping("/complete")
	public ApiResponse<SessionCompleteResponseDto> completeSession(
			@RequestHeader("Authorization") String authorizationHeader,
			@Valid @RequestBody SessionCompleteRequestDto request) {
		
		// JWT 토큰에서 사용자 ID 추출
		String token = extractTokenFromHeader(authorizationHeader);
		Long userId = jwtUtil.getUserIdFromToken(token);
		
		if (userId == null) {
			throw new ApiHandler(ErrorStatus._UNAUTHORIZED);
		}
		
		log.info("러닝 완료 요청: userId={}, duration={}, distance={}, parkName={}", 
				userId, request.getDuration(), request.getDistance(), request.getParkName());
		
		SessionCompleteResponseDto response = runningService.completeSession(userId, request);
		return ApiResponse.onSuccess(response);
	}
	
	private String extractTokenFromHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new ApiHandler(ErrorStatus._UNAUTHORIZED);
		}
		return authorizationHeader.substring(7);
	}
}
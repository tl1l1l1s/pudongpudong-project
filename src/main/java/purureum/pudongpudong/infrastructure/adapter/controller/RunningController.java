package purureum.pudongpudong.infrastructure.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import purureum.pudongpudong.application.service.RunningService;
import purureum.pudongpudong.global.apiPayload.ApiResponse;
import purureum.pudongpudong.global.util.AuthUtil;
import purureum.pudongpudong.infrastructure.dto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/running")
@Tag(name = "Running API", description = "러닝 기록 관련 API")
public class RunningController {
	
	private final RunningService runningService;
	private final AuthUtil authUtil;
	
	@Operation(
			summary = "캘린더 러닝 기록 조회",
			description = "지정된 연도와 월의 사용자 러닝 기록을 조회합니다."
	)
	@GetMapping
	public ApiResponse<RunningResponseDto> getRunningCalendar(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
			@RequestParam int year,
			@RequestParam int month) {
		return ApiResponse.onSuccess(runningService.getRunningCalendar(authUtil.extractUserIdFromHeader(authorizationHeader), year, month));
	}
	
	@Operation(
			summary = "러닝 완료",
			description = "러닝 세션을 완료하고 결과를 저장합니다."
	)
	@PostMapping("/complete")
	public ApiResponse<SessionCompleteResponseDto> completeSession(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
			@Valid @RequestBody SessionCompleteRequestDto request) {
		
		Long userId = authUtil.extractUserIdFromHeader(authorizationHeader);
		
		log.info("러닝 완료 요청: userId={}, duration={}, distance={}, parkName={}", 
				userId, request.getDuration(), request.getDistance(), request.getParkName());
		
		SessionCompleteResponseDto response = runningService.completeSession(userId, request);
		return ApiResponse.onSuccess(response);
	}
}
package purureum.pudongpudong.infrastructure.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import purureum.pudongpudong.application.service.query.RunningQueryService;
import purureum.pudongpudong.application.service.command.RunningCommandService;
import purureum.pudongpudong.application.service.VoiceAssistantService;
import purureum.pudongpudong.global.apiPayload.ApiResponse;
import purureum.pudongpudong.global.util.AuthUtil;
import purureum.pudongpudong.infrastructure.dto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/running")
@Tag(name = "Running API", description = "러닝 기록 관련 API")
public class RunningController {
	
	private final RunningQueryService runningQueryService;
	private final RunningCommandService runningCommandService;
	private final VoiceAssistantService voiceAssistantService;
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
		return ApiResponse.onSuccess(runningQueryService.getRunningCalendar(authUtil.extractUserIdFromHeader(authorizationHeader), year, month));
	}
	
	@Operation(
			summary = "러닝 완료",
			description = "러닝 세션을 완료하고 결과를 저장합니다."
	)
	@PostMapping("/complete")
	public ApiResponse<SessionCompleteResponseDto> completeSession(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
			@Valid @RequestBody SessionCompleteRequestDto request) {
		return ApiResponse.onSuccess(runningCommandService.completeSession(authUtil.extractUserIdFromHeader(authorizationHeader), request));
	}
	
	@Operation(
			summary = "음성 보조",
			description = "음성 질의를 처리하여 답변을 생성합니다."
	)
	@PostMapping("/voice-assistant")
	public ApiResponse<VoiceResponseDto> processVoiceQuery(@Valid @RequestBody VoiceRequestDto request) {
		return ApiResponse.onSuccess(
			voiceAssistantService.processVoiceQuery(request)
				.map(VoiceResponseDto::new)
				.block()
		);
	}
}
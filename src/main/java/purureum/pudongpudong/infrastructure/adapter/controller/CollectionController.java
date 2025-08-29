package purureum.pudongpudong.infrastructure.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import purureum.pudongpudong.application.service.query.CollectionQueryService;
import purureum.pudongpudong.global.apiPayload.ApiResponse;
import purureum.pudongpudong.global.util.AuthUtil;
import purureum.pudongpudong.infrastructure.dto.CollectionResponseDto;
import purureum.pudongpudong.infrastructure.dto.TrainerDetailResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collection")
@Tag(name = "Collection API", description = "컬렉션 현황 관련 API입니다.")
public class CollectionController {
	
	private final AuthUtil authUtil;
	private final CollectionQueryService collectionQueryService;
	
	@Operation(
			summary = "컬렉션 현황 조회",
			description = "사용자의 트레이너 및 스탬프 컬렉션 현황을 조회합니다."
	)
	@GetMapping
	public ApiResponse<CollectionResponseDto> getCollectionStatus(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
		return ApiResponse.onSuccess(collectionQueryService.getCollectionStatus(authUtil.extractUserIdFromHeader(authorizationHeader)));
	}

	@Operation(
			summary = "트레이너 상세 조회",
			description = "사용자의 특정 트레이너 상세 정보를 조회합니다."
	)
	@GetMapping("/{trainerId}")
	public ApiResponse<TrainerDetailResponseDto> getTrainerDetail(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader,
			@PathVariable Long trainerId) {
		Long userId = authUtil.extractUserIdFromHeader(authorizationHeader);
		return ApiResponse.onSuccess(collectionQueryService.getTrainerDetail(userId, trainerId));
	}
}
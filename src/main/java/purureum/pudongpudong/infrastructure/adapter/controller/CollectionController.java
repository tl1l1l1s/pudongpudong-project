package purureum.pudongpudong.infrastructure.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import purureum.pudongpudong.application.service.CollectionService;
import purureum.pudongpudong.global.apiPayload.ApiResponse;
import purureum.pudongpudong.global.util.AuthUtil;
import purureum.pudongpudong.infrastructure.dto.CollectionResponseDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collection")
@Tag(name = "Collection API", description = "컬렉션 현황 관련 API입니다.")
public class CollectionController {
	
	private final CollectionService collectionService;
	private final AuthUtil authUtil;
	
	@Operation(
			summary = "컬렉션 현황 조회",
			description = "사용자의 트레이너 및 스탬프 컬렉션 현황을 조회합니다."
	)
	@GetMapping
	public ApiResponse<CollectionResponseDto> getCollectionStatus(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
		return ApiResponse.onSuccess(collectionService.getCollectionStatus(authUtil.extractUserIdFromHeader(authorizationHeader)));
	}
	
}
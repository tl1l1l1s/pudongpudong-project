package purureum.pudongpudong.infrastructure.adapter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import purureum.pudongpudong.application.service.query.HomeQueryService;
import purureum.pudongpudong.global.apiPayload.ApiResponse;
import purureum.pudongpudong.global.util.AuthUtil;
import purureum.pudongpudong.infrastructure.dto.HomeResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
@Tag(name = "Home API", description = "홈 화면 관련 API입니다.")
public class HomeController {

    private final HomeQueryService homeQueryService;
    private final AuthUtil authUtil;

    @Operation(summary = "홈 화면 주간 활동 조회", description = "홈 화면에 표시될 사용자의 이번 주 활동 및 통계를 조회합니다.")
    @GetMapping
    public ApiResponse<HomeResponseDto> getHomeData(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = authUtil.extractUserIdFromHeader(authorizationHeader);
        return ApiResponse.onSuccess(homeQueryService.getHomeData(userId));
    }
}

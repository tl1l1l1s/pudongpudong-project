package purureum.pudongpudong.infrastructure.adapter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import purureum.pudongpudong.global.apiPayload.ApiResponse;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {
	
	@GetMapping
	public ApiResponse<String> healthCheck() {
		return ApiResponse.onSuccess("서버가 건강합니다!");
	}
}
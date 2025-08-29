package purureum.pudongpudong.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import purureum.pudongpudong.infrastructure.adapter.api.GeminiApiService;
import purureum.pudongpudong.infrastructure.adapter.api.KakaoMapApiService;
import purureum.pudongpudong.infrastructure.dto.VoiceRequestDto;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VoiceAssistantService {

	private final GeminiApiService geminiApiService;
	private final KakaoMapApiService kakaoMapApiService;

	public Mono<String> processVoiceQuery(VoiceRequestDto request) {
		String query = request.getQuery().toLowerCase();
		
		if (query.contains("시간") || query.contains("남았")) {
			return generateTimeResponse(request);
		} else if (query.contains("거리") || query.contains("킬로")) {
			return generateDistanceResponse(request);
		} else if (query.contains("페이스") || query.contains("속도")) {
			return generatePaceResponse(request);
		} else if (query.contains("화장실") || query.contains("편의점") || query.contains("주변")) {
			return generateLocationResponse(request, query);
		} else {
			return generateGeneralResponse(request);
		}
	}

	private Mono<String> generateTimeResponse(VoiceRequestDto request) {
		Integer minutes = request.getRemainingTimeMinutes();
		Integer seconds = request.getRemainingTimeSeconds();
		
		if (minutes == null) minutes = 0;
		if (seconds == null) seconds = 0;
		
		String prompt = String.format(
			"러닝 중인 사용자에게 남은 시간이 %d분 %d초라고 알려주면서 격려해줘. 50자 이내로 간단하게.",
			minutes, seconds
		);
		
		return geminiApiService.generateResponse(prompt);
	}

	private Mono<String> generateDistanceResponse(VoiceRequestDto request) {
		Double distance = request.getCurrentDistance();
		if (distance == null) distance = 0.0;
		
		String prompt = String.format(
			"러닝 중인 사용자에게 지금까지 %.2fkm 뛰었다고 알려주면서 격려해줘. 50자 이내로 간단하게.",
			distance
		);
		
		return geminiApiService.generateResponse(prompt);
	}

	private Mono<String> generatePaceResponse(VoiceRequestDto request) {
		Double pace = request.getCurrentPace();
		if (pace == null) pace = 0.0;
		
		String prompt = String.format(
			"러닝 중인 사용자에게 현재 페이스가 %.2f분/km라고 알려주면서 격려해줘. 50자 이내로 간단하게.",
			pace
		);
		
		return geminiApiService.generateResponse(prompt);
	}

	private Mono<String> generateLocationResponse(VoiceRequestDto request, String query) {
		String prompt = String.format(
			"러닝 중인 사용자가 '%s'에 대해 물어봤어. 주변 시설을 찾아보고 있다고 안내하며 격려해줘. 30자 이내로 간단하게.",
			query
		);
		
		return geminiApiService.generateResponse(prompt);
	}

	private Mono<String> generateGeneralResponse(VoiceRequestDto request) {
		String prompt = String.format(
			"러닝 중인 사용자가 '%s'라고 말했어. 러닝과 관련된 격려 메시지를 30자 이내로 간단하게 해줘.",
			request.getQuery()
		);
		
		return geminiApiService.generateResponse(prompt);
	}
}
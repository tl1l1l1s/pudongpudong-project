package purureum.pudongpudong.infrastructure.adapter.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeminiApiService {

	private final WebClient webClient;
	private final String apiKey;

	public GeminiApiService(WebClient.Builder webClientBuilder,
							@Value("${gemini.api.url}") String apiUrl,
							@Value("${gemini.api.key}") String apiKey) {
		this.webClient = webClientBuilder.baseUrl(apiUrl).build();
		this.apiKey = apiKey;
	}

	public Mono<String> generateResponse(String prompt) {
		Map<String, Object> requestBody = Map.of(
			"contents", List.of(
				Map.of("parts", List.of(Map.of("text", prompt)))
			)
		);

		return webClient.post()
				.uri("?key=" + apiKey)
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(Map.class)
				.map(response -> {
					try {
						List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
						if (candidates != null && !candidates.isEmpty()) {
							Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
							if (content != null) {
								List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
								if (parts != null && !parts.isEmpty()) {
									return (String) parts.get(0).get("text");
								}
							}
						}
					} catch (Exception e) {
						log.error("Gemini response parsing error", e);
					}
					return "죄송해요, 지금은 답변하기 어려워요.";
				})
				.onErrorReturn("음성 서비스가 일시적으로 불안정해요. 잠시 후 다시 시도해주세요.");
	}
}
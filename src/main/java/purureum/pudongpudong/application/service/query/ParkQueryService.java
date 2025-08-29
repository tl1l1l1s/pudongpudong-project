package purureum.pudongpudong.application.service.query;

import purureum.pudongpudong.infrastructure.dto.ParkResponseDto;
import reactor.core.publisher.Flux;

public interface ParkQueryService {
	Flux<ParkResponseDto> getAllParksWithMyLocation(Double longitude, Double latitude);
}

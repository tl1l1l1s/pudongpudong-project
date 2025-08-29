package purureum.pudongpudong.application.service.command;

import purureum.pudongpudong.domain.model.Parks;
import reactor.core.publisher.Flux;

public interface ParkCommandService {
	Flux<Parks> saveAllParks();
}

package purureum.pudongpudong.application.command;

import purureum.pudongpudong.domain.model.Parks;
import reactor.core.publisher.Flux;

public interface ParkCommandService {
	Flux<Parks> saveAllParks();
}

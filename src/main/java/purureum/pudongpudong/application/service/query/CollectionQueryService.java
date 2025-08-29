package purureum.pudongpudong.application.service.query;

import purureum.pudongpudong.infrastructure.dto.*;

public interface CollectionQueryService {
	CollectionResponseDto getCollectionStatus(Long userId);
}
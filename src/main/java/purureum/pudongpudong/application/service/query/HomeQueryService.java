package purureum.pudongpudong.application.service.query;

import purureum.pudongpudong.infrastructure.dto.HomeResponseDto;

public interface HomeQueryService {
    HomeResponseDto getHomeData(Long userId);
}

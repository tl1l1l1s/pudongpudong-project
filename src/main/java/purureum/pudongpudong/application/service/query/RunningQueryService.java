package purureum.pudongpudong.application.service.query;

import purureum.pudongpudong.infrastructure.dto.*;

public interface RunningQueryService {
	RunningResponseDto getRunningCalendar(Long userId, int year, int month);
}
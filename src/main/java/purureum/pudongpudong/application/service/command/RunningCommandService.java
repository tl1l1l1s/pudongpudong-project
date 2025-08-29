package purureum.pudongpudong.application.service.command;

import purureum.pudongpudong.infrastructure.dto.*;

public interface RunningCommandService {
	SessionCompleteResponseDto completeSession(Long userId, SessionCompleteRequestDto request);
}
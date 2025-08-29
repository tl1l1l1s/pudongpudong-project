package purureum.pudongpudong.application.service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.*;
import purureum.pudongpudong.domain.repository.ParksRepository;
import purureum.pudongpudong.domain.repository.SessionsRepository;
import purureum.pudongpudong.domain.repository.UserStampsRepository;
import purureum.pudongpudong.domain.repository.UsersRepository;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.handler.ApiHandler;
import purureum.pudongpudong.global.util.MathUtil;
import purureum.pudongpudong.infrastructure.dto.SessionCompleteRequestDto;
import purureum.pudongpudong.infrastructure.dto.SessionCompleteResponseDto;
import purureum.pudongpudong.infrastructure.dto.StampDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RunningCommandServiceImpl implements RunningCommandService {
	
	private final SessionsRepository sessionsRepository;
	private final UsersRepository usersRepository;
	private final ParksRepository parksRepository;
	private final UserStampsRepository userStampsRepository;
	private final Random random = new Random();
	
	@Override
	public SessionCompleteResponseDto completeSession(Long userId, SessionCompleteRequestDto request) {
		validateSessionRequest(request);
		
		Users user = findUserById(userId);
		Parks park = findParkByName(request.getParkName());
		
		Sessions session = createSession(user, park, request);
		user.getUserStatistics().updateRunStatistics(request.getDistance(), request.getCalories());
		usersRepository.save(user);
		
		double pace = MathUtil.calculatePace(request.getDuration(), request.getDistance());
		
		log.info("러닝 완료: userId={}, duration={}, distance={}, parkName={}",
				userId, request.getDuration(), request.getDistance(), request.getParkName());
		
		return SessionCompleteResponseDto.builder()
				.sessionId(session.getId())
				.parkName(request.getParkName())
				.duration(request.getDuration())
				.distance(request.getDistance())
				.pace(pace)
				.calories(request.getCalories())
				.mood(request.getMood().name().toLowerCase())
				.stamps(assignRandomStamps(session))
				.build();
	}
	
	private Sessions createSession(Users user, Parks park, SessionCompleteRequestDto request) {
		Sessions session = Sessions.builder()
				.user(user)
				.park(park)
				.duration(request.getDuration())
				.distance(request.getDistance())
				.caloriesBurned(request.getCalories())
				.mood(request.getMood())
				.build();
		return sessionsRepository.save(session);
	}
	
	private List<StampDto> assignRandomStamps(Sessions session) {
		List<Species> availableSpecies = userStampsRepository.findSpeciesByParkName(
				session.getPark().getPlaceName());
		
		if (availableSpecies.isEmpty()) {
			log.warn("공원에 등록된 수목이 없습니다: parkName={}", session.getPark().getPlaceName());
			return new ArrayList<>();
		}
		
		Species selectedSpecies = availableSpecies.get(random.nextInt(availableSpecies.size()));
		userStampsRepository.save(UserStamps.builder()
				.user(session.getUser())
				.species(selectedSpecies)
				.session(session)
				.collectedAt(LocalDateTime.now())
				.build());
		
		log.info("랜덤 스탬프 할당: userId={}, sessionId={}, species={}",
				session.getUser().getId(), session.getId(), selectedSpecies.getName());
		
		return List.of(StampDto.builder()
						.name(selectedSpecies.getName())
						.emoji(selectedSpecies.getEmoji())
				.build());
	}
	
	private void validateSessionRequest(SessionCompleteRequestDto request) {
		if (request.getDuration() == null || request.getDuration() <= 0) {
			throw new ApiHandler(ErrorStatus.INVALID_SESSION_DATA);
		}
		if (request.getDistance() == null || request.getDistance() <= 0) {
			throw new ApiHandler(ErrorStatus.INVALID_SESSION_DATA);
		}
		if (request.getCalories() == null || request.getCalories() <= 0) {
			throw new ApiHandler(ErrorStatus.INVALID_SESSION_DATA);
		}
		if (request.getParkName() == null || request.getParkName().trim().isEmpty()) {
			throw new ApiHandler(ErrorStatus.INVALID_SESSION_DATA);
		}
		if (request.getMood() == null) {
			throw new ApiHandler(ErrorStatus.INVALID_SESSION_DATA);
		}
	}
	
	private Users findUserById(Long userId) {
		return usersRepository.findById(userId)
				.orElseThrow(() -> new ApiHandler(ErrorStatus.USER_NOT_FOUND));
	}
	
	private Parks findParkByName(String parkName) {
		return parksRepository.findByPlaceName(parkName)
				.orElseThrow(() -> new ApiHandler(ErrorStatus.PARK_NOT_FOUND));
	}
}

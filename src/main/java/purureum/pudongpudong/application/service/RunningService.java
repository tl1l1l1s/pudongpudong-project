package purureum.pudongpudong.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.*;
import purureum.pudongpudong.domain.repository.*;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.custom.ParkNotFoundException;
import purureum.pudongpudong.global.apiPayload.exception.custom.UserNotFoundException;
import purureum.pudongpudong.global.util.MathUtil;
import purureum.pudongpudong.infrastructure.dto.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunningService {
	
	private final SessionsRepository sessionsRepository;
	private final UsersRepository usersRepository;
	private final ParksRepository parksRepository;
	private final UserStampsRepository userStampsRepository;
	private final UserStatisticsRepository userStatisticsRepository;
	private final Random random;
	
	@Transactional(readOnly = true)
	public RunningResponseDto getRunningCalendar(Long userId, int year, int month) {
		RunningStatisticsDto statistics = sessionsRepository.findStatisticsByUserIdAndYearMonth(userId, year, month);
		List<Sessions> sessions = sessionsRepository.findByUserIdAndYearMonth(userId, year, month);
		List<RunningDataDto> data = buildRunningData(sessions);
		
		return RunningResponseDto.builder()
				.statistics(statistics)
				.data(data)
				.build();
	}
	
	private List<RunningDataDto> buildRunningData(List<Sessions> sessions) {
		Map<Integer, Sessions> dailySessions = new LinkedHashMap<>();
		
		for (Sessions session : sessions) {
			int day = session.getCreatedAt().getDayOfMonth();
			if (!dailySessions.containsKey(day)) {
				dailySessions.put(day, session);
			}
		}
		
		List<RunningDataDto> result = new ArrayList<>();
		
		for (Map.Entry<Integer, Sessions> entry : dailySessions.entrySet()) {
			Integer day = entry.getKey();
			Sessions session = entry.getValue();
			
			double pace = MathUtil.calculatePace(session.getDuration(), session.getDistance());
			
			List<UserStamps> stamps = userStampsRepository.findBySessionId(session.getId());
			List<String> stampNames = stamps.stream()
					.map(stamp -> stamp.getSpecies().getName())
					.collect(Collectors.toList());
			
			RunningDataDto dto = RunningDataDto.builder()
					.date(day)
					.duration(session.getDuration() != null ? session.getDuration() : 0)
					.distance(session.getDistance() != null ? session.getDistance() : 0.0)
					.pace(pace)
					.calories(session.getCaloriesBurned() != null ? session.getCaloriesBurned() : 0.0)
					.location(session.getPark() != null ? session.getPark().getPlaceName() : "")
					.mood(session.getMood() != null ? session.getMood().name().toLowerCase() : "")
					.stamps(stampNames)
					.build();
			
			result.add(dto);
		}
		
		return result;
	}
	
	@Transactional
	public SessionCompleteResponseDto completeSession(Long userId, SessionCompleteRequestDto request) {
		Users user = findUserById(userId);
		Parks park = findParkByName(request.getParkName());
		
		Sessions session = createSession(user, park, request);
		updateUserStatistics(user, request.getDistance(), request.getCalories());
		List<String> stampNames = assignRandomStamps(session);
		
		double pace = MathUtil.calculatePace(request.getDuration(), request.getDistance());
		
		log.info("러닝 세션 완료: userId={}, sessionId={}, stamps={}", userId, session.getId(), stampNames);
		
		return SessionCompleteResponseDto.builder()
				.sessionId(session.getId())
				.duration(request.getDuration())
				.distance(request.getDistance())
				.pace(pace)
				.calories(request.getCalories())
				.parkName(request.getParkName())
				.mood(request.getMood().name().toLowerCase())
				.stamps(stampNames)
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
	
	private List<String> assignRandomStamps(Sessions session) {
		List<Species> availableSpecies = userStampsRepository.findSpeciesByParkName(
				session.getPark().getPlaceName());
		
		if (availableSpecies.isEmpty()) {
			return new ArrayList<>();
		}
		
		Species selectedSpecies = selectRandomSpecies(availableSpecies);
		createUserStamp(session, selectedSpecies);
		
		return List.of(selectedSpecies.getName());
	}
	
	private Users findUserById(Long userId) {
		return usersRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(ErrorStatus.USER_NOT_FOUND));
	}
	
	private Parks findParkByName(String parkName) {
		return parksRepository.findByPlaceName(parkName)
				.orElseThrow(() -> new ParkNotFoundException(ErrorStatus.PARK_NOT_FOUND));
	}
	
	private Species selectRandomSpecies(List<Species> availableSpecies) {
		return availableSpecies.get(random.nextInt(availableSpecies.size()));
	}
	
	private void createUserStamp(Sessions session, Species species) {
		UserStamps userStamp = UserStamps.builder()
				.user(session.getUser())
				.session(session)
				.species(species)
				.collectedAt(LocalDateTime.now())
				.build();
		userStampsRepository.save(userStamp);
	}
	
	private void updateUserStatistics(Users user, Double distance, Double calories) {
		UserStatistics statistics = userStatisticsRepository.findByUserId(user.getId())
				.orElse(null);
		
		if (statistics == null) {
			statistics = UserStatistics.createInitial(user);
		}
		
		statistics.updateRunStatistics(distance, calories);
		statistics.addStamp();
		
		userStatisticsRepository.save(statistics);
		log.info("사용자 통계 업데이트 완료: userId={}, totalRuns={}, totalDistance={}", 
				user.getId(), statistics.getRunCount(), statistics.getTotalDistance());
	}
}
package purureum.pudongpudong.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.*;
import purureum.pudongpudong.domain.repository.*;
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
	
	@Transactional(readOnly = true)
	public RunningResponseDto getRunningCalendar(Long userId, int year, int month) {
		// 통계 데이터 조회
		Object[] statisticsData = sessionsRepository.findStatisticsByUserIdAndYearMonth(userId, year, month);
		RunningStatisticsDto statistics = buildStatistics(statisticsData);
		
		// 세션 데이터 조회
		List<Sessions> sessions = sessionsRepository.findByUserIdAndYearMonth(userId, year, month);
		List<RunningDataDto> data = buildRunningData(sessions);
		
		return RunningResponseDto.builder()
				.statistics(statistics)
				.data(data)
				.build();
	}
	
	private RunningStatisticsDto buildStatistics(Object[] data) {
		if (data == null || data.length != 3) {
			return RunningStatisticsDto.builder()
					.running(0)
					.distance(0.0)
					.calories(0.0)
					.build();
		}
		
		Long runningCount = (Long) data[0];
		Double totalDistance = (Double) data[1];
		Double totalCalories = (Double) data[2];
		
		return RunningStatisticsDto.builder()
				.running(runningCount != null ? runningCount.intValue() : 0)
				.distance(totalDistance != null ? totalDistance : 0.0)
				.calories(totalCalories != null ? totalCalories : 0.0)
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
			
			double pace = 0.0;
			if (session.getDistance() != null && session.getDistance() > 0 && session.getDuration() != null) {
				pace = session.getDuration() / session.getDistance();
				pace = Math.round(pace * 100.0) / 100.0; // 소수점 2자리
			}
			
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
		Users user = usersRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
		
		Parks park = parksRepository.findByPlaceName(request.getParkName())
				.orElseThrow(() -> new RuntimeException("공원을 찾을 수 없습니다."));
		
		Sessions session = Sessions.builder()
				.user(user)
				.park(park)
				.duration(request.getDuration())
				.distance(request.getDistance())
				.caloriesBurned(request.getCalories())
				.mood(request.getMood())
				.build();
		
		session = sessionsRepository.save(session);
		
		updateUserStatistics(user, request.getDistance(), request.getCalories());
		
		List<String> stampNames = assignRandomStamps(session);
		
		double pace = request.getDistance() > 0 ? request.getDuration() / request.getDistance() : 0.0;
		
		log.info("러닝 세션 완료: userId={}, sessionId={}, stamps={}", userId, session.getId(), stampNames);
		
		return SessionCompleteResponseDto.builder()
				.sessionId(session.getId())
				.duration(request.getDuration())
				.distance(request.getDistance())
				.pace(Math.round(pace * 100.0) / 100.0) // 소수점 2자리
				.calories(request.getCalories())
				.parkName(request.getParkName())
				.mood(request.getMood().name().toLowerCase())
				.stamps(stampNames)
				.build();
	}
	
	private List<String> assignRandomStamps(Sessions session) {
		List<Species> availableSpecies = userStampsRepository.findRandomSpeciesByParkName(
				session.getPark().getPlaceName());
		
		if (availableSpecies.isEmpty()) {
			return new ArrayList<>();
		}
		
		// 진짜 랜덤으로 선택
		Random random = new Random();
		Species selectedSpecies = availableSpecies.get(random.nextInt(availableSpecies.size()));
		
		UserStamps userStamp = UserStamps.builder()
				.user(session.getUser())
				.session(session)
				.species(selectedSpecies)
				.collectedAt(LocalDateTime.now())
				.build();
		
		userStampsRepository.save(userStamp);
		
		return List.of(selectedSpecies.getName());
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
package purureum.pudongpudong.application.service.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.Sessions;
import purureum.pudongpudong.domain.model.UserStamps;
import purureum.pudongpudong.domain.repository.SessionsRepository;
import purureum.pudongpudong.domain.repository.UserStampsRepository;
import purureum.pudongpudong.global.util.MathUtil;
import purureum.pudongpudong.infrastructure.dto.RunningDataDto;
import purureum.pudongpudong.infrastructure.dto.RunningResponseDto;
import purureum.pudongpudong.infrastructure.dto.RunningStatisticsDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunningQueryServiceImpl implements RunningQueryService{
	
	private final SessionsRepository sessionsRepository;
	private final UserStampsRepository userStampsRepository;
	
	@Override
	public RunningResponseDto getRunningCalendar(Long userId, int year, int month) {
		List<Sessions> sessions = sessionsRepository.findByUserIdAndYearMonth(userId, year, month);
		
		RunningStatisticsDto statistics = buildStatistics(sessions);
		List<RunningDataDto> data = buildRunningData(sessions);
		
		return RunningResponseDto.builder()
				.statistics(statistics)
				.data(data)
				.build();
	}
	
	private RunningStatisticsDto buildStatistics(List<Sessions> sessions) {
		Integer running = sessions.size();
		Double distance = sessions.stream()
				.mapToDouble(s -> s.getDistance() != null ? s.getDistance() : 0.0)
				.sum();
		Double calories = sessions.stream()
				.mapToDouble(s -> s.getCaloriesBurned() != null ? s.getCaloriesBurned() : 0.0)
				.sum();
		
		return RunningStatisticsDto.builder()
				.running(running)
				.distance(distance)
				.calories(calories)
				.build();
	}
	
	private List<RunningDataDto> buildRunningData(List<Sessions> sessions) {
		Map<Integer, Sessions> dailySessions = sessions.stream()
				.collect(Collectors.toMap(
						session -> session.getCreatedAt().getDayOfMonth(),
						session -> session,
						(existing, replacement) -> existing
				));
		
		List<RunningDataDto> data = new ArrayList<>();
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
			
			data.add(dto);
		}
		
		return data;
	}
}

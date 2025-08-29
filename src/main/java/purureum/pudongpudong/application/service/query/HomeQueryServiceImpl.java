package purureum.pudongpudong.application.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.Sessions;
import purureum.pudongpudong.domain.model.UserStamps;
import purureum.pudongpudong.domain.repository.UserStampsRepository;
import purureum.pudongpudong.infrastructure.dto.HomeResponseDto;
import purureum.pudongpudong.infrastructure.dto.RecentActivityDto;
import purureum.pudongpudong.infrastructure.dto.WeeklyStatisticsDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeQueryServiceImpl implements HomeQueryService {

    private final UserStampsRepository userStampsRepository;

    @Override
    public HomeResponseDto getHomeData(Long userId) {
        LocalDate today = LocalDate.now();

        List<UserStamps> weeklyStamps = userStampsRepository.findByUserAndDateRangeWithDetails(userId,
				today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay(),
				today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(23, 59, 59));

        WeeklyStatisticsDto statistics = buildWeeklyStatistics(weeklyStamps);
        List<RecentActivityDto> recentActivities = buildRecentActivities(weeklyStamps);

        return HomeResponseDto.builder()
                .statistics(statistics)
                .recentActivities(recentActivities)
                .build();
    }

    private WeeklyStatisticsDto buildWeeklyStatistics(List<UserStamps> weeklyStamps) {
        if (weeklyStamps.isEmpty()) {
            return WeeklyStatisticsDto.builder()
                    .running(0)
                    .distance(0.0)
                    .collectedStamps(0)
                    .build();
        }

        int runningCount = (int) weeklyStamps.stream().map(stamp -> stamp.getSession().getId()).distinct().count();
        double totalDistance = weeklyStamps.stream()
                .map(UserStamps::getSession)
                .distinct()
                .mapToDouble(Sessions::getDistance)
                .sum();
        int collectedStampsCount = weeklyStamps.size();

        return WeeklyStatisticsDto.builder()
                .running(runningCount)
                .distance(totalDistance)
                .collectedStamps(collectedStampsCount)
                .build();
    }

    private List<RecentActivityDto> buildRecentActivities(List<UserStamps> weeklyStamps) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return weeklyStamps.stream()
                .map(stamp -> RecentActivityDto.builder()
                        .stamp(stamp.getSpecies().getName())
                        .parkName(stamp.getSession().getPark().getPlaceName())
						.parkEmoji(stamp.getSession().getPark().getEmoji())
                        .visitedAt(stamp.getCreatedAt().format(formatter))
                        .duration(stamp.getSession().getDuration())
                        .distance(stamp.getSession().getDistance())
                        .build())
                .collect(Collectors.toList());
    }
}

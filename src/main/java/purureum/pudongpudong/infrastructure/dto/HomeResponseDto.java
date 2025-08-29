package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeResponseDto {
    private final WeeklyStatisticsDto statistics;
    private final List<RecentActivityDto> recentActivities;
}

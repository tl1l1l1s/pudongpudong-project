package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecentActivityDto {
    private final String stamp;
    private final String parkName;
	private final String parkEmoji;
    private final String visitedAt;
    private final Integer duration;
    private final Double distance;
}

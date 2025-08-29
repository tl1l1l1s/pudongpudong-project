package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StampDto {
    private final String emoji;
    private final String name;
}

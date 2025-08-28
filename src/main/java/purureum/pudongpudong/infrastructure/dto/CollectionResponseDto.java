package purureum.pudongpudong.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CollectionResponseDto {
	private CollectionOverviewDto overview;
	private List<CollectionTrainerDto> trainers;
}
package purureum.pudongpudong.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.UserTrainers;
import purureum.pudongpudong.domain.repository.*;
import purureum.pudongpudong.global.util.MathUtil;
import purureum.pudongpudong.infrastructure.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionService {
	
	private final UserTrainersRepository userTrainersRepository;
	private final UserStampsRepository userStampsRepository;
	private final TrainersRepository trainersRepository;
	private final ParkSpeciesRepository parkSpeciesRepository;
	
	@Transactional(readOnly = true)
	public CollectionResponseDto getCollectionStatus(Long userId) {
		CollectionOverviewDto overview = buildOverview(userId);
		List<CollectionTrainerDto> trainers = buildTrainerCollection(userId);
		
		log.info("컬렉션 현황 조회: userId={}, unlockedTrainers={}, collectedStamps={}", 
				userId, overview.getUnlockedTrainers(), overview.getCollectedStamps());
		
		return CollectionResponseDto.builder()
				.overview(overview)
				.trainers(trainers)
				.build();
	}
	
	private CollectionOverviewDto buildOverview(Long userId) {
		Integer unlockedTrainers = userTrainersRepository.countByUserId(userId);
		long totalTrainers = trainersRepository.count();
		Integer lockedTrainers = (int) totalTrainers - unlockedTrainers;
		
		Integer collectedStamps = userStampsRepository.countDistinctSpeciesByUserId(userId);
		Integer totalStamps = userStampsRepository.countTotalSpecies();
		
		double completionPercentage = MathUtil.calculateCompletionPercentage(collectedStamps, totalStamps);
		
		return CollectionOverviewDto.builder()
				.unlockedTrainers(unlockedTrainers)
				.lockedTrainers(lockedTrainers)
				.collectedStamps(collectedStamps)
				.completionPercentage(completionPercentage)
				.build();
	}
	
	private List<CollectionTrainerDto> buildTrainerCollection(Long userId) {
		List<UserTrainers> userTrainers = userTrainersRepository.findByUserId(userId);
		
		if (userTrainers.isEmpty()) {
			return new ArrayList<>();
		}
		
		Map<String, Integer> stampsMap = userStampsRepository.countSpeciesByUserIdAndPark(userId);
		List<CollectionTrainerDto> result = new ArrayList<>();
		
		for (UserTrainers userTrainer : userTrainers) {
			String parkName = userTrainer.getTrainer().getName();
			Integer totalSpecies = parkSpeciesRepository.countByParkName(parkName);
			Integer collectedSpecies = stampsMap.getOrDefault(parkName, 0);
			
			CollectionTrainerDto dto = CollectionTrainerDto.builder()
					.parkName(parkName)
					.trainerName(userTrainer.getTrainer().getName())
					.level(userTrainer.getLevel())
					.experience(userTrainer.getExperience())
					.totalSpecies(totalSpecies)
					.collectedSpecies(collectedSpecies)
					.build();
			
			result.add(dto);
		}
		
		return result;
	}
}
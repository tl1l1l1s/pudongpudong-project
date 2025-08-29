package purureum.pudongpudong.application.service.query;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.*;
import purureum.pudongpudong.domain.repository.ParkSpeciesRepository;
import purureum.pudongpudong.domain.repository.TrainersRepository;
import purureum.pudongpudong.domain.repository.UserStampsRepository;
import purureum.pudongpudong.domain.repository.UserTrainersRepository;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.handler.ApiHandler;
import purureum.pudongpudong.global.util.MathUtil;
import purureum.pudongpudong.infrastructure.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionQueryServiceImpl implements CollectionQueryService {
	
	private final UserTrainersRepository userTrainersRepository;
	private final UserStampsRepository userStampsRepository;
	private final TrainersRepository trainersRepository;
	private final ParkSpeciesRepository parkSpeciesRepository;
	
	@Override
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
	
	@Override
	public TrainerDetailResponseDto getTrainerDetail(Long userId, Long trainerId) {
		Trainers trainer = findTrainerById(trainerId);
		UserTrainers userTrainer = findUserTrainerById(userId, trainerId);

		if (trainer.getPark() == null) {
			throw new ApiHandler(ErrorStatus.TRAINER_PARK_NOT_ASSIGNED);
		}

		String parkId = trainer.getPark().getId();
		List<Species> allParkSpecies = findSpeciesByParkId(parkId);

		if (allParkSpecies.isEmpty()) {
			throw new ApiHandler(ErrorStatus.NO_SPECIES_IN_PARK);
		}

		List<Species> collectedParkSpecies = findUserCollectedSpeciesByUserIdAndParkId(userId, parkId);

		List<TrainerStampDto> stamps = buildTrainerStamps(allParkSpecies, collectedParkSpecies);

		log.info("트레이너 상세 조회: userId={}, trainerId={}, trainerName={}, collectedStamps={}, totalStamps={}",
				userId, trainerId, trainer.getName(), collectedParkSpecies.size(), allParkSpecies.size());

		return TrainerDetailResponseDto.builder()
				.trainerName(trainer.getName())
				.description(trainer.getDescription())
				.stamps(stamps)
				.collectedStamps(collectedParkSpecies.size())
				.totalStamps(allParkSpecies.size())
				.build();
	}

	private List<TrainerStampDto> buildTrainerStamps(List<Species> allParkSpecies, List<Species> collectedParkSpecies) {
		java.util.Set<Long> collectedSpeciesIds = collectedParkSpecies.stream()
				.map(Species::getId)
				.collect(java.util.stream.Collectors.toSet());

		return allParkSpecies.stream()
				.map(species -> TrainerStampDto.builder()
						.speciesName(species.getName())
						.emoji(species.getEmoji())
						.isCollected(collectedSpeciesIds.contains(species.getId()))
						.build())
				.collect(java.util.stream.Collectors.toList());
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
			Parks park = userTrainer.getTrainer().getPark();
			if (park == null) {
				log.warn("트레이너에 공원이 할당되지 않았습니다: trainerId={}", userTrainer.getTrainer().getId());
				continue;
			}

			String parkName = park.getPlaceName();
			Integer totalSpecies = parkSpeciesRepository.countByParkId(park.getId());
			Integer collectedSpecies = stampsMap.getOrDefault(parkName, 0);
			double completionPercentage = MathUtil.calculateCompletionPercentage(collectedSpecies, totalSpecies);

			CollectionTrainerDto dto = CollectionTrainerDto.builder()
					.parkName(parkName)
					.trainerId(userTrainer.getTrainer().getId())
					.trainerName(userTrainer.getTrainer().getName())
					.collectedSpecies(collectedSpecies)
					.totalSpecies(totalSpecies)
					.completionPercentage(completionPercentage)
					.build();

			result.add(dto);
		}

		return result;
	}
	
	private Trainers findTrainerById(Long trainerId) {
		return trainersRepository.findById(trainerId)
				.orElseThrow(() -> new ApiHandler(ErrorStatus.TRAINER_NOT_FOUND));
	}
	
	private UserTrainers findUserTrainerById(Long userId, Long trainerId) {
		return userTrainersRepository.findByUserIdAndTrainerId(userId, trainerId)
				.orElseThrow(() -> new ApiHandler(ErrorStatus.USER_TRAINER_NOT_FOUND));
	}
	
	private List<Species> findSpeciesByParkId(String parkId) {
		return parkSpeciesRepository.findSpeciesByParkId(parkId);
	}
	
	private List<Species> findUserCollectedSpeciesByUserIdAndParkId(Long userId, String parkId) {
		return userStampsRepository.findCollectedSpeciesByUserIdAndParkId(userId, parkId);
	}
}
